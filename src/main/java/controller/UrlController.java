package controller;

import exception.InvalidUrlException;
import exception.ShortCodeNotFoundException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import model.ShortUrl;
import service.ShortenerService;

import java.util.Map;

/**
 * HTTP layer. Registers:
 *   POST /api/shorten  -> 201 + short URL (or 400 on InvalidUrlException)
 *   GET  /{code}        -> 302 redirect (or 404 on ShortCodeNotFoundException)
 */

public class UrlController {

    private final ShortenerService service;

    public UrlController(ShortenerService service){
        this.service = service;

    }

    public void registerRoutes(Javalin app){
        app.post("/api/shorten", this::handleShorten);
        app.get("/{code}", this::handleRedirect);

    }

    private void handleShorten(Context ctx) {
        try {
            String longUrl = ctx.bodyAsClass(ShortenRequest.class).url();
            ShortUrl shortUrl = service.shorten(longUrl);
            ctx.status(201).json(shortUrl);
        } catch (InvalidUrlException e) {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }

    private void handleRedirect(Context ctx) {
        String code = ctx.pathParam("code");
        try {
            ShortUrl shortUrl = service.resolve(code);
            ctx.redirect(shortUrl.longUrl(), HttpStatus.FOUND);
        } catch (ShortCodeNotFoundException e) {
            ctx.status(404).json(Map.of("error", e.getMessage()));
        }
    }

    private record ShortenRequest(String url) {}
}
