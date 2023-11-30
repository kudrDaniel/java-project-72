package hexlet.code.controller;

import io.javalin.Javalin;

@FunctionalInterface
public interface Routed {
    void route(Javalin app);
}
