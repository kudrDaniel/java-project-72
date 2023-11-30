package hexlet.code.dto;

import hexlet.code.provider.FlashProvider.Flash;
import lombok.Getter;

@Getter
public final class RootPage extends BasePage {
    private final String url;

    public RootPage(Flash flash, String url) {
        super(flash);
        this.url = url;
    }
}
