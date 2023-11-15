package hexlet.code;

import hexlet.code.controller.RootController;
import hexlet.code.util.Database;
import hexlet.code.util.Environment;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public final class App {
    public static void main(String[] args) throws SQLException, IOException {
        Database.init();

        Javalin app = App.getApp();

        app.start(Environment.getPort());
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create();

        app.before(ctx -> ctx.contentType("text/html; charset=utf-8"));

        app.get(NamedRoutes.rootPath(), RootController::index);

        return app;
    }
}
