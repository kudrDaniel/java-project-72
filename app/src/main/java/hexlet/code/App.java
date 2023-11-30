package hexlet.code;

import hexlet.code.controller.ChecksController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.Routed;
import hexlet.code.controller.UrlController;
import hexlet.code.provider.DatabaseProvider;
import hexlet.code.provider.JteTemplateProvider;
import hexlet.code.util.Environment;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public final class App {
    private static final List<Routed> CONTROLLERS = List.of(
            RootController.getINSTANCE(),
            UrlController.getINSTANCE(),
            ChecksController.getINSTANCE()
    );

    public static void main(String[] args) {
        Javalin app = App.getApp();

        app.start(Environment.PORT.getValue());
    }

    public static Javalin getApp() {
        JavalinJte.init(JteTemplateProvider.createTemplateEngine());

        DatabaseProvider.createConnectionPool();
        DatabaseProvider.initializeDB();

        var app = Javalin.create();

        app.before(ctx -> ctx.contentType("text/html; charset=utf-8"));

        CONTROLLERS.forEach(controller -> controller.route(app));

        return app;
    }
}
