package hexlet.code;

import io.javalin.Javalin;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    @Test
    public void applicationTest() {
        Javalin application = App.getApp();

        assertThat(application).isNotNull();
    }
}
