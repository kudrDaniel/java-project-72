package hexlet.code;

import hexlet.code.controller.ChecksController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import hexlet.code.util.DataSourceHelper;
import hexlet.code.util.EnvironmentHelper;
import hexlet.code.util.JteTemplateHelper;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public final class App {
    public static void main(String[] args) throws SQLException {
        Javalin app = App.getApp();

        app.start(EnvironmentHelper.getPort());
    }

    public static Javalin getApp() throws SQLException {
        DataSourceHelper.init();

        JavalinJte.init(JteTemplateHelper.createTemplateEngine());

        Javalin app = Javalin.create();

        app.before(ctx -> ctx.contentType("text/html; charset=utf-8"));

        app.get(NamedRoutes.rootPath(), RootController::index);

        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.urlsPath(), UrlController::index);

        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);

        app.post(NamedRoutes.checksPath("{id}"), ChecksController::create);

        return app;
    }
}
