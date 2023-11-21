package hexlet.code.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.App;
import hexlet.code.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public final class Database {
    public static void init() throws SQLException {
        log.info("Data source initialization start...");
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(Environment.getJdbcUrl());

        HikariDataSource dataSource;

        try {
            dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            log.info("Error while data source initialization, switch to in-memory database", e);
            hikariConfig.setJdbcUrl(Environment.getMemJdbc());
            dataSource = new HikariDataSource(hikariConfig);
        }

        var is = App.class.getClassLoader().getResourceAsStream("schema.sql");

        String sql = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            sql = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;
    }
}
