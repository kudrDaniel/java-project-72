package hexlet.code.controller;

import hexlet.code.dto.RootPage;
import hexlet.code.util.Flash;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
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

    public static void addUrl(Context ctx) {
        var rawUrl = ctx.formParam("url");
        try {
            var parsedUrl = new URL(rawUrl);
            ctx.sessionAttribute("flashType", Flash.alertSuccess());
            ctx.sessionAttribute("flashMessage", "Страница успешно добавлена");
            ctx.redirect(NamedRoutes.rootPath());
        } catch (MalformedURLException e) {
            log.error(String.format("Error occurred while parsing \"%s\"%s", rawUrl, e.getMessage()));
            var page = new RootPage(rawUrl);
            page.setFlash(new Flash(Flash.alertDanger(), "Некорректныйы URL"));
            ctx.render("index.jte", Collections.singletonMap("page", page));
        } catch (NullPointerException e) {
            log.error(String.format("URL cannot be NULL%s", e.getMessage()));
            var page = new RootPage(rawUrl);
            page.setFlash(new Flash(Flash.alertDanger(), "Некорректный URL"));
            ctx.render("index.jte", Collections.singletonMap("page", page));
        }
    }
}
