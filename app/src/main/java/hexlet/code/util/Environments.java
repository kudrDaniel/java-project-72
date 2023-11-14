package hexlet.code.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Environments {
    private static Integer applicationPort = null;

    public static Integer getApplicationPort() {
        if (applicationPort == null) {
            var env = System.getenv("PA_PORT");
            var parsed = 7080;
            try {
                parsed = Integer.parseInt(env);
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
                log.info("Set application port to default \"8080\"");
            }
            applicationPort = parsed;
        }
        return applicationPort;
    }
}
