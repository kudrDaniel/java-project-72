package hexlet.code.controller;

import hexlet.code.dto.RootPage;
import io.javalin.http.Context;

import java.util.Collections;

public final class RootController {
    public static void index(Context ctx) {
        var page = new RootPage();
        page.setFlash(null);
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
