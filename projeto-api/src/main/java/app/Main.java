package app;

import io.javalin.Javalin;
import java.time.Instant;
import java.util.*;

public class Main {
    static List<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        app.get("/status", ctx -> {
            ctx.json(Map.of("status", "ok", "timestamp", Instant.now().toString()));
        });

        app.post("/echo", ctx -> {
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            ctx.json(body);
        });

        app.get("/saudacao/{nome}", ctx -> {
            String nome = ctx.pathParam("nome");
            ctx.json(Map.of("mensagem", "Olá, " + nome + "!"));
        });

        app.post("/usuarios", ctx -> {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuarios.add(usuario);
            ctx.status(201);
        });

        app.get("/usuarios", ctx -> {
            ctx.json(usuarios);
        });

        app.get("/usuarios/{email}", ctx -> {
            String email = ctx.pathParam("email");
            usuarios.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("Usuário não encontrado")
                );
        });
    }
}