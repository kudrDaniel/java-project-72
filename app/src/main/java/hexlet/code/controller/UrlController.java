package hexlet.code.controller;

import hexlet.code.dto.RootPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.Flash;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
public class UrlController {
    public static void index(Context ctx) {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(new Flash(
                ctx.consumeSessionAttribute("flashType"),
                ctx.consumeSessionAttribute("flashMessage")
        ));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + "not found"));
        var checks = CheckRepository.getEntitiesByUrlId(id);
        var page = new UrlPage(url, checks);
        page.setFlash(new Flash(
                ctx.consumeSessionAttribute("flashType"),
                ctx.consumeSessionAttribute("flashMessage")
        ));
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) {
        var rawUrl = ctx.formParam("url");
        try {
            var parsedUrl = new URI(rawUrl).toURL();
            var url = new Url();
            url.setName(parsedUrl.toString());
            url.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            UrlRepository.save(url);
            ctx.sessionAttribute("flashType", Flash.alertSuccess());
            ctx.sessionAttribute("flashMessage", "Страница успешно добавлена");
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (JdbcSQLIntegrityConstraintViolationException e) {
            ctx.sessionAttribute("flashType", Flash.alertInfo());
            ctx.sessionAttribute("flashMessage", "Страница уже существует");
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (Exception e) {
            log.error("Error occurred while parsing url: {}", rawUrl, e);
            var page = new RootPage(rawUrl);
            page.setFlash(new Flash(Flash.alertDanger(), "Некорректный URL"));
            ctx.render("index.jte", Collections.singletonMap("page", page));
        }
    }
}
