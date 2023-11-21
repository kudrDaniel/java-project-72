package hexlet.code.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Environment {
    public static final String PORT_ENV = "PORT";
    public static final String DB_ENV = "JDBC_DATABASE_URL";

    static final String PORT_DEF = "7090";

    public static int getPort() {
        String raw = System.getenv().getOrDefault(PORT_ENV, PORT_DEF);
        log.info("From environment " + PORT_ENV + "readed string " + raw);
        int result = Integer.parseInt(PORT_DEF);
        try {
            result = Integer.parseInt(raw);
            log.info("Port set to: " + result);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            log.info("Port set to default: " + PORT_DEF);
        }
        return result;
    }

    public static String getJdbcUrl() {
        return System.getenv().getOrDefault(DB_ENV, getMemJdbc());
    }

    public static String getMemJdbc() {
        return "jdbc:h2:mem:seo_page;DB_CLOSE_DELAY=-1;";
    }
}
