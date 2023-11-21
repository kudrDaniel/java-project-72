package hexlet.code;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class AppTest {

    @Test
    public void test() {
        assertThat(true).isTrue();
    }
}
