package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class RootPage extends BasePage {
    private String url;
}
