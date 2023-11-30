package hexlet.code.controller;

import hexlet.code.dto.RootPage;
import hexlet.code.provider.FlashProvider.Flash;
import io.javalin.Javalin;
import io.javalin.http.Context;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public final class RootController implements Routed {
    @Getter
    private static final RootController INSTANCE;

    static {
        INSTANCE = new RootController();
    }

    private RootController() {

    }

    public String rootPath() {
        return "/";
    }

    public void index(Context ctx) {
        var flash = new Flash(
                ctx.consumeSessionAttribute("flashType"),
                ctx.consumeSessionAttribute("flashMessage")
        );
        var url = ctx.queryParamAsClass("url", String.class).getOrDefault("");
        var page = new RootPage(flash, url);
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

    @Override
    public void route(Javalin app) {
        app.get(rootPath(), INSTANCE::index);
    }
}
