package hexlet.code.controller;

import hexlet.code.model.Check;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.Flash;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
public class ChecksController {
    public static void create(Context ctx) {
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
                log.error("{}", "Error while get h1 text occurred", e);
                check.setHeader("");
            }
            try {
                check.setDescription(
                        html.selectFirst("meta[name=description]")
                                .attributes().get("content")
                );
            } catch (NullPointerException e) {
                log.error("{}", "Error while get description occurred", e);
                check.setDescription("");
            }
            CheckRepository.save(check);

            ctx.sessionAttribute("flashType", Flash.alertSuccess());
            ctx.sessionAttribute("flashMessage", "Страница успешно проверена");
        } catch (UnirestException e) {
            log.error("{}", "Error while get response occurred", e);
            ctx.sessionAttribute("flashType", Flash.alertDanger());
            ctx.sessionAttribute("flashMessage", "Некорректный адрес");
        } finally {
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
