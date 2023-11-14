package hexlet.code.util;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class Environments {
    private static Integer APPLICATION_PORT = null;

    public static Integer getApplicationPort() {
        if (APPLICATION_PORT == null) {
            var env = System.getenv("PA_PORT");
            var parsed = 7080;
            try {
                parsed = Integer.parseInt(env);
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
                log.info("Set application port to default \"7080\"");
            }
            APPLICATION_PORT = parsed;
        }
        return APPLICATION_PORT;
    }
}
