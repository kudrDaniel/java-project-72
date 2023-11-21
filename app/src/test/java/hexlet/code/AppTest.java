package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.util.TestHelper;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private static MockWebServer mockServer;
    private Javalin app;
    private Map<String, Object> existingUrl;
    private Map<String, Object> existingUrlCheck;
    private HikariDataSource dataSource;

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse()
                .setBody(TestHelper.readFixture("index.html"));
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        app = App.getApp();

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(TestHelper.getDatabaseUrl());

        dataSource = new HikariDataSource(hikariConfig);

        var is = App.class.getClassLoader().getResourceAsStream("schema.sql");

        String sql = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            sql = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw e;
        }

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        String url = "https://localhost";

        TestHelper.addUrl(dataSource, url, Timestamp.valueOf(LocalDateTime.now()));
        existingUrl = TestHelper.getUrlByName(dataSource, url);

        TestHelper.addUrlCheck(dataSource, (long) existingUrl.get("id"), Timestamp.valueOf(LocalDateTime.now()));
        existingUrlCheck = TestHelper.getUrlCheck(dataSource, (long) existingUrl.get("id"));
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
                        .contains(existingUrl.get("name").toString())
                        .contains(existingUrlCheck.get("status_code").toString());
            });
        }

        @Test
        void testShow() {
            JavalinTest.test(app, (server, client) -> {
                var response = client.get("/urls/" + existingUrl.get("id"));
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains(existingUrl.get("name").toString())
                        .contains(existingUrlCheck.get("status_code").toString());
            });
        }

        @Test
        void testStore() {

            String inputUrl = "https://ru.hexlet.io";

            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + inputUrl;
                assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

                var response = client.get("/urls");
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains(inputUrl);

                var actualUrl = TestHelper.getUrlByName(dataSource, inputUrl);
                assertThat(actualUrl).isNotNull();
                assertThat(actualUrl.get("name").toString()).isEqualTo(inputUrl);
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

                var actualUrl = TestHelper.getUrlByName(dataSource, url);
                assertThat(actualUrl).isNotNull();

                assertThat(actualUrl.get("name").toString()).isEqualTo(url);

                client.post("/urls/" + actualUrl.get("id") + "/checks");

                assertThat(client.get("/urls/" + actualUrl.get("id")).code())
                        .isEqualTo(200);

                var actualCheck = TestHelper.getUrlCheck(dataSource, (long) actualUrl.get("id"));
                assertThat(actualCheck).isNotNull();
                assertThat(actualCheck.get("title")).isEqualTo("Todd Howard quotes");
                assertThat(actualCheck.get("h1")).isEqualTo("It just works!");
                assertThat(actualCheck.get("description")).isEqualTo("quotes by genius game-designer");
            });
        }
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }
}
