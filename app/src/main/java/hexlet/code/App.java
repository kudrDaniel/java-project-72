package hexlet.code;

import hexlet.code.controller.RootController;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class App {
    public static void main(String[] args) {
        Javalin app = App.getApp();

        app.start(7080);
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create();

        app.get(NamedRoutes.rootPath(), RootController::index);

        return app;
    }
}
