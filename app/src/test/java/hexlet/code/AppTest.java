package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.EnvironmentHelper;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private static MockWebServer mockServer;
    private Javalin app;
    private HikariDataSource dataSource;

    @BeforeAll
    public static void beforeAll() throws IOException {
        var fileName = "index.html";
        var filePath = Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse()
                .setBody(Files.readString(filePath).trim());
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        app = App.getApp();

        var hikariConfig = new HikariConfig();
        var jdbcDatabaseUrl = EnvironmentHelper.getJdbcUrl();
        hikariConfig.setJdbcUrl(jdbcDatabaseUrl);

        dataSource = new HikariDataSource(hikariConfig);

        var dbInit = App.class.getClassLoader().getResourceAsStream("schema.sql");
        String dbInitQuery = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dbInit, StandardCharsets.UTF_8))) {
            dbInitQuery = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw e;
        }

        var dbScript = App.class.getClassLoader().getResourceAsStream("script.sql");
        String dbScriptQuery = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dbScript, StandardCharsets.UTF_8))) {
            dbScriptQuery = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw e;
        }

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(dbInitQuery);
            statement.execute(dbScriptQuery);
        }
    }

    @Nested
    class RootTest {
        @Test
        void testIndex() {
            JavalinTest.test(app, (server, client) -> {
                assertThat(client.get("/").code()).isEqualTo(200);
            });
        }
    }

    @Nested
    class UrlTest {
        @Test
        void testIndex() {
            JavalinTest.test(app, (server, client) -> {
                var response = client.get("/urls");
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains("https://localhost")
                        .contains("200");
            });
        }

        @Test
        void testShow() {
            JavalinTest.test(app, (server, client) -> {
                var response = client.get("/urls/" + 1);
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains("https://localhost")
                        .contains("200");
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "https://localhost:8080",
            "https://localhost:7070/about",
            "https://localhost:7076/about/share"
        })
        void testStore(String inputUrl) {
            var endIndex = StringUtils.ordinalIndexOf(inputUrl, "/", 3);
            if (endIndex == -1) {
                endIndex = inputUrl.length();
            }
            var expectedUrl = inputUrl.substring(0, endIndex);

            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + inputUrl;
                assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

                var response = client.get("/urls");
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains(expectedUrl);

                var statement = "SELECT * FROM urls WHERE name = ?";
                try (var connection = dataSource.getConnection();
                     var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.setString(1, expectedUrl);
                    var resultSet = preparedStatement.executeQuery();
                    assertThat(resultSet.next()).isTrue();
                    assertThat(resultSet.getString("name")).isEqualTo(expectedUrl);
                }
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "https://localhost/about",
            "https://localhost:8080",
            "https://localhost:7070/info",
            "https://localhost:7076/info/share"
        })
        void testDuplicateStore(String inputUrl) {
            var endIndex = StringUtils.ordinalIndexOf(inputUrl, "/", 3);
            if (endIndex == -1) {
                endIndex = inputUrl.length();
            }
            var expectedUrl = inputUrl.substring(0, endIndex);

            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + inputUrl;
                var response = client.post("/urls", requestBody);
                assertThat(response.priorResponse()).isNotNull();
                assertThat(response.priorResponse().code()).isEqualTo(302);

                var statement = "SELECT * FROM urls WHERE name = ?";
                try (var connection = dataSource.getConnection();
                     var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.setString(1, expectedUrl);
                    var resultSet = preparedStatement.executeQuery();
                    assertThat(resultSet.next()).isTrue();
                    assertThat(resultSet.next()).isFalse();
                }
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "jgkfld",
            "https:/localhost:8080",
            "ftp//localhost:7070/info",
            "https://localhost:789653/info/share"
        })
        void testWrongStore(String inputUrl) {
            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + inputUrl;
                var response = client.post("/urls", requestBody);
                assertThat(response.code()).isEqualTo(200);

                var statement = "SELECT * FROM urls WHERE name = ?";
                try (var connection = dataSource.getConnection();
                     var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.setString(1, inputUrl);
                    var resultSet = preparedStatement.executeQuery();
                    assertThat(resultSet.next()).isFalse();
                }
            });
        }
    }

    @Nested
    class UrlCheckTest {
        @Test
        void testStore() {
            String url = mockServer.url("/").toString().replaceAll("/$", "");

            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + url;
                assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

                var actualUrl = UrlRepository.findByName(url);
                assertThat(actualUrl).isNotNull();

                assertThat(actualUrl.get().getName().toString()).isEqualTo(url);

                client.post("/urls/" + actualUrl.get().getId() + "/checks");

                assertThat(client.get("/urls/" + actualUrl.get().getId()).code())
                        .isEqualTo(200);

                var actualCheck = CheckRepository.getLastByUrlId(actualUrl.get().getId());
                assertThat(actualCheck).isNotNull();
                assertThat(actualCheck.get().getTitle()).isEqualTo("Todd Howard quotes");
                assertThat(actualCheck.get().getHeader()).isEqualTo("It just works!");
                assertThat(actualCheck.get().getDescription()).isEqualTo("quotes by genius game-designer");
            });
        }
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }
}
