package controller;

/**
 * HTTP layer. Registers:
 *   POST /api/shorten  -> 201 + short URL (or 400 on InvalidUrlException)
 *   GET  /{code}        -> 302 redirect (or 404 on ShortCodeNotFoundException)
 */

public class UrlController {
}
