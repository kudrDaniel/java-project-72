package hexlet.code.controller;

import hexlet.code.dto.RootPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.provider.FlashProvider;
import hexlet.code.provider.FlashProvider.Flash;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.exception.UrlSavingException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
public final class UrlController implements Routed {
    @Getter
    private static final UrlController INSTANCE;

    static {
        INSTANCE = new UrlController();
    }

    private UrlController() {

    }

    public String urlsPath() {
        return "/urls";
    }

    public String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public String urlPath(String id) {
        return urlsPath() + "/" + id;
    }

    public void index(Context ctx) {
        var flash = new Flash(
                ctx.consumeSessionAttribute("flashType"),
                ctx.consumeSessionAttribute("flashMessage")
        );
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(flash, urls);
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public void create(Context ctx) {
        var rawUrl = ctx.formParam("url");
        try {
            var parsedUrl = "";
            try {
                var fullUrl = new URI(rawUrl).toURL();
                if (fullUrl.getAuthority() == null || fullUrl.getPort() > 65535) {
                    throw new Exception("Authority cannot be null or wrong port number");
                }
                parsedUrl = String.format(
                        "%s://%s",
                        fullUrl.getProtocol(),
                        fullUrl.getAuthority()
                );
            } catch (Exception e) {
                throw new UrlSavingException(
                        e.getMessage(),
                        UrlSavingException.State.URL_INCORRECT
                );
            }
            var url = new Url();
            url.setName(parsedUrl);
            url.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            UrlRepository.save(url);
            ctx.sessionAttribute("flashType", FlashProvider.ALERT_SUCCESS);
            ctx.sessionAttribute("flashMessage", "Страница успешно добавлена");
            ctx.redirect(urlsPath());
        } catch (UrlSavingException e) {
            switch (e.getState()) {
                case URL_INCORRECT -> {
                    log.error("Error occurred while parsing url: {}", rawUrl, e);
                    var flash = new Flash(
                            ctx.consumeSessionAttribute(FlashProvider.ALERT_DANGER),
                            ctx.consumeSessionAttribute("Некорректный URL")
                    );
                    var page = new RootPage(flash, rawUrl);
                    ctx.render("index.jte", Collections.singletonMap("page", page));
                }
                case URL_EXISTS -> {
                    ctx.sessionAttribute("flashType", FlashProvider.ALERT_INFO);
                    ctx.sessionAttribute("flashMessage", "Страница уже существует");
                    ctx.redirect(urlsPath());
                }
                default -> throw new RuntimeException();
            }

        }
    }

    public void show(Context ctx) {
        var flash = new Flash(
                ctx.consumeSessionAttribute("flashType"),
                ctx.consumeSessionAttribute("flashMessage")
        );
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + "not found"));
        var checks = CheckRepository.getEntitiesByUrlId(id);
        var page = new UrlPage(flash, url, checks);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    @Override
    public void route(Javalin app) {
        app.get(urlsPath(), INSTANCE::index);
        app.post(urlsPath(), INSTANCE::create);
        app.get(urlPath("{id}"), INSTANCE::show);
    }
}
