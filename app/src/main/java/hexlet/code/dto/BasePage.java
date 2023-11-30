package hexlet.code.dto;

import hexlet.code.provider.FlashProvider.Flash;
import lombok.Getter;

@Getter
public class BasePage {
    private final Flash flash;

    public BasePage(Flash flash) {
        this.flash = flash;
    }
}
