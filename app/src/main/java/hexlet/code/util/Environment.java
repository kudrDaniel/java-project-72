package hexlet.code.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Environment {
    public static int getPort() {
        String raw = System.getenv().getOrDefault("PORT", "7090");
        int result = 7090;
        try {
            result = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            log.info("Port set to default: 7090");
        }
        return result;
    }

    public static String getJdbcUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", getMemJdbc());
    }

    public static String getMemJdbc() {
        return "jdbc:h2:mem:seo_page;DB_CLOSE_DELAY=-1;";
    }
}
