package hexlet.code.util;

import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TestHelper {
    public static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }

    public static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    public static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:seo_page");
    }

    public static void addUrl(HikariDataSource dataSource, String url, Timestamp createdAt) throws SQLException {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, url);
            stmt.setTimestamp(2, createdAt);
            var resultSet = stmt.executeUpdate();
        }
    }

    public static Map<String, Object> getUrlByName(HikariDataSource dataSource, String url) throws SQLException {
        var result = new HashMap<String, Object>();
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, url);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                result.put("id", resultSet.getLong("id"));
                result.put("name", resultSet.getString("name"));
                return result;
            }

            return null;
        }
    }

    public static void addUrlCheck(HikariDataSource dataSource, long urlId, Timestamp createdAt) throws SQLException {
        var sql = "INSERT INTO url_checks (url_id, status_code, title, description, h1, created_at)"
                + "VALUES (?, 200, 'en title', 'en description', 'en h1', ?)";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            stmt.setTimestamp(2, createdAt);
            var resultSet = stmt.executeUpdate();
        }
    }

    public static Map<String, Object> getUrlCheck(HikariDataSource dataSource, long urlId) throws SQLException {
        var result = new HashMap<String, Object>();
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                result.put("id", resultSet.getLong("id"));
                result.put("url_id", resultSet.getLong("url_id"));
                result.put("status_code", resultSet.getInt("status_code"));
                result.put("title", resultSet.getString("title"));
                result.put("h1", resultSet.getString("h1"));
                result.put("description", resultSet.getString("description"));
                return result;
            }

            return null;
        }
    }
}
