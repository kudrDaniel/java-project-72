package hexlet.code.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Environment {
    public static int getPort() {
        String raw = System.getenv().getOrDefault("PORT", "7070");
        int result = 7070;
        try {
            result = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            log.info("Port set to default: 7070");
        }
        return result;
    }

    public static String getJdbcUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:seo_page");
    }
}
