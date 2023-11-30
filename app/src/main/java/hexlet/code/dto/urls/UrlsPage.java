package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;
import hexlet.code.provider.FlashProvider.Flash;
import lombok.Getter;

import java.util.List;

@Getter
public class UrlsPage extends BasePage {
    private final List<Url> urls;

    public UrlsPage(Flash flash, List<Url> urls) {
        super(flash);
        this.urls = urls;
    }
}
