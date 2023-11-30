package hexlet.code.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Getter
@Slf4j
public abstract class Environment<T> {
    public static final Environment<Integer> PORT = new Environment<>(
            "PORT", 7080) {
        @Override
        Optional<Integer> getEnv(String def) {
            String value = System.getenv(def);
            try {
                int parsed = Integer.parseInt(value);
                if (parsed > 0x0 && parsed <= 0xffff) {
                    return Optional.of(parsed);
                } else {
                    log.error("Port between 1 and 65535 inclusively allowed!\n"
                            + "Actual: \"" + parsed + "\"");
                    return Optional.empty();
                }
            } catch (Exception e) {
                log.error("Cannot parse string: \"" + value + "\"\n", e);
                return Optional.empty();
            }
        }
    };

    public static final Environment<String> JDBC_DATABASE_URL = new Environment<>(
            "JDBC_DATABASE_URL", "jdbc:h2:mem:seo_page_dev") {
        @Override
        Optional<String> getEnv(String def) {
            String value = System.getenv(def);
            if (value == null || DEVELOPMENT.getValue()) {
                return Optional.empty();
            } else {
                return Optional.of(value);
            }
        }
    };

    public static final Environment<Boolean> DEVELOPMENT = new Environment<>(
            "DEVELOPMENT", false) {
        @Override
        Optional<Boolean> getEnv(String def) {
            if (System.getProperty(def).equals("true")) {
                return Optional.of(true);
            }
            return Optional.empty();
        }
    };

    private final String definition;

    private T value;

    Environment(String def, T defaultValue) {
        definition = def;
        try {
            value = this.getEnv(def).orElseThrow();
        } catch (Exception e) {
            noSuchEnvironmentString(String.valueOf(defaultValue), e);
            value = defaultValue;
        }
    }

    abstract Optional<T> getEnv(String def);

    /**
     * @param value default value of environment
     * @param e cause exception
     */
    private void noSuchEnvironmentString(String value, Throwable e) {
        log.error(String.format(
                """
                        Environment with key %s not found
                        Set default value: %s""",
                definition, value
        ), e);
    }
}
