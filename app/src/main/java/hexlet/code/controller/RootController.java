package hexlet.code.controller;

import hexlet.code.dto.RootPage;
import hexlet.code.util.Flash;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public final class RootController {
    public static void index(Context ctx) {
        var page = new RootPage("");
        page.setFlash(new Flash(
                ctx.consumeSessionAttribute("flashType"),
                ctx.consumeSessionAttribute("flashMessage")
        ));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
