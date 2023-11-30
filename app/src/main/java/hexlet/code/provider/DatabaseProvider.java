package hexlet.code.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.App;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.Environment;
import hexlet.code.util.Envs;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public final class DatabaseProvider {
    public static void createConnectionPool() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(Envs.getJdbcUrl());

        HikariDataSource connectionPool = null;
        try {
            connectionPool = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            log.error(String.format(
                    """
                            Can't create connection pool
                            Check %s environment variable""",
                    Environment.JDBC_DATABASE_URL.getDefinition()
            ), e);
        }

        BaseRepository.connectionPool = connectionPool;
    }

    public static void initializeDB() {
        var is = App.class.getClassLoader().getResourceAsStream("schema.sql");

        String sql = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            sql = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info(sql);
        try (var connection = BaseRepository.connectionPool.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
