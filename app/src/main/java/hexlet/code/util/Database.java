package hexlet.code.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.App;
import hexlet.code.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public final class Database {
    public static void init() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(Environment.getJdbcUrl() + ";DB_CLOSE_DELAY=-1;");

        var dataSource = new HikariDataSource(hikariConfig);
        URI uri = null;
        try {
            uri = App.class.getClassLoader().getResource("schema.sql").toURI();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }
        var file = new File(uri);
        var sql = Files.lines(file.toPath(), StandardCharsets.UTF_8)
                .collect(Collectors.joining("\n"));

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;
    }
}
