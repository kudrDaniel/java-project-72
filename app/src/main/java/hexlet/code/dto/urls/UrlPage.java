package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Check;
import hexlet.code.model.Url;
import hexlet.code.provider.FlashProvider.Flash;
import lombok.Getter;

import java.util.List;

@Getter
public class UrlPage extends BasePage {
    private final Url url;
    private final List<Check> checks;

    public UrlPage(Flash flash, Url url, List<Check> checks) {
        super(flash);
        this.url = url;
        this.checks = checks;
    }
}
