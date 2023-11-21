package hexlet.code.repository;

import hexlet.code.model.Url;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public final class UrlRepository extends BaseRepository {
    public static void save(Url url) throws JdbcSQLIntegrityConstraintViolationException {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var name = url.getName();
            if (findByName(name).isPresent()) {
                throw new SQLException(String.format("Entity with name=%s already exists", name));
            }

            preparedStatement.setString(1, name);
            preparedStatement.setTimestamp(2, url.getCreatedAt());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned keys after saving an entity");
            }
        } catch (JdbcSQLIntegrityConstraintViolationException e) {
            log.error("Url with same name exists", e);
            throw e;
        } catch (SQLException e) {
            log.error("Error while saving url", e);
        }
    }

    public static Optional<Url> findById(Long id) {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var url = new Url();
                url.setId(resultSet.getLong("id"));
                url.setName(resultSet.getString("name"));
                url.setCreatedAt(resultSet.getTimestamp("created_at"));
                url.setLast(CheckRepository.getLastByUrlId(url.getId()).orElse(null));
                return Optional.of(url);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("{}", "Error while finding url by id", e);
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, name);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var url = new Url();
                url.setId(resultSet.getLong("id"));
                url.setName(resultSet.getString("name"));
                url.setCreatedAt(resultSet.getTimestamp("created_at"));
                url.setLast(CheckRepository.getLastByUrlId(url.getId()).orElse(null));
                return Optional.of(url);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("{}", "Error while finding url by name", e);
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() {
        var sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var url = new Url();
                url.setId(resultSet.getLong("id"));
                url.setName(resultSet.getString("name"));
                url.setCreatedAt(resultSet.getTimestamp("created_at"));
                url.setLast(CheckRepository.getLastByUrlId(url.getId()).orElse(null));
                result.add(url);
            }
            return result;
        } catch (SQLException e) {
            log.error("{}", "Error while getting urls", e);
            return List.of();
        }
    }
}
