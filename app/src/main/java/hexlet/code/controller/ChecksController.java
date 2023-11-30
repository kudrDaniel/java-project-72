package hexlet.code.controller;

import hexlet.code.model.Check;
import hexlet.code.provider.FlashProvider;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
public final class ChecksController implements Routed {
    @Getter
    private static final ChecksController INSTANCE;

    static {
        INSTANCE = new ChecksController();
    }

    private ChecksController() {

    }

    public String checksPath(Long id) {
        return checksPath(String.valueOf(id));
    }

    public String checksPath(String id) {
        return UrlController.getINSTANCE().urlPath(id) + "/checks";
    }

    public void create(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + urlId + "not found"));
        var check = new Check();

        try {
            HttpResponse<String> response = Unirest
                    .get(url.getName())
                    .asString();
            Document html = Jsoup.parse(response.getBody());

            check.setUrlId(urlId);
            check.setStatus(response.getStatus());
            check.setTitle(html.title());
            try {
                check.setHeader(html.selectFirst("h1").text());
            } catch (NullPointerException e) {
                log.error("Error while get h1 text occurred", e);
                check.setHeader("");
            }
            try {
                check.setDescription(
                        html.selectFirst("meta[name=description]")
                                .attributes().get("content")
                );
            } catch (NullPointerException e) {
                log.error("Error while get description occurred", e);
                check.setDescription("");
            }
            check.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            CheckRepository.save(check);

            ctx.sessionAttribute("flashType", FlashProvider.ALERT_SUCCESS);
            ctx.sessionAttribute("flashMessage", "Страница успешно проверена");
        } catch (Exception e) {
            log.error("Error while checking occurred", e);
            ctx.sessionAttribute("flashType", FlashProvider.ALERT_DANGER);
            ctx.sessionAttribute("flashMessage", "Некорректный адрес");
        } finally {
            ctx.redirect(UrlController.getINSTANCE().urlPath(urlId));
        }
    }

    @Override
    public void route(Javalin app) {
        app.post(checksPath("{id}"), INSTANCE::create);
    }
}
