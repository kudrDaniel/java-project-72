package hexlet.code.repository;

import hexlet.code.model.Check;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CheckRepository extends BaseRepository {
    public static void save(Check check) {
        var sql = "INSERT INTO checks (url_id, status, title, header, description) VALUES (?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, check.getUrlId());
            preparedStatement.setInt(2, check.getStatus());
            preparedStatement.setString(3, check.getTitle());
            preparedStatement.setString(4, check.getHeader());
            preparedStatement.setString(5, check.getDescription());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
                check.setCreatedAt(generatedKeys.getTimestamp(2));
            } else {
                throw new SQLException("DB have not returned keys after saving an entity");
            }
        } catch (SQLException e) {
            log.error("{}", "Error while saving check", e);
        }
    }

    public static Optional<Check> getLastByUrlId(Long urlId) {
        var sql = "SELECT * FROM checks WHERE url_id = ? ORDER BY id DESC LIMIT 1";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var check = new Check();
                check.setId(resultSet.getLong("id"));
                check.setUrlId(resultSet.getLong("url_id"));
                check.setStatus(resultSet.getInt("status"));
                check.setTitle(resultSet.getString("title"));
                check.setHeader(resultSet.getString("header"));
                check.setDescription(resultSet.getString("description"));
                check.setCreatedAt(resultSet.getTimestamp("created_at"));
                return Optional.of(check);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("{}", "Error while finding url by id", e);
            return Optional.empty();
        }
    }

    public static List<Check> getEntitiesByUrlId(Long urlId) {
        var sql = "SELECT * FROM checks WHERE url_id = ? ORDER BY id ASC";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);
            var resultSet = preparedStatement.executeQuery();
            var result = new ArrayList<Check>();
            while (resultSet.next()) {
                var check = new Check();
                check.setId(resultSet.getLong("id"));
                check.setUrlId(resultSet.getLong("url_id"));
                check.setStatus(resultSet.getInt("status"));
                check.setTitle(resultSet.getString("title"));
                check.setHeader(resultSet.getString("header"));
                check.setDescription(resultSet.getString("description"));
                check.setCreatedAt(resultSet.getTimestamp("created_at"));
                result.add(check);
            }
            return result;
        } catch (SQLException e) {
            log.error("{}", "Error while getting urls", e);
            return List.of();
        }
    }
}