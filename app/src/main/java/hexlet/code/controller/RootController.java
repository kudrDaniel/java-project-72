package hexlet.code.controller;

import hexlet.code.dto.regular.RootPage;
import io.javalin.http.Context;

import java.util.Collections;

import static hexlet.code.util.Timestamp.getTimestamp;

public final class RootController {
    public static void index(Context ctx) {
        var page = new RootPage();
        page.setDate(getTimestamp());
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
