package service;

/**
 * Core business logic: validate, shorten, resolve, and handle the
 * duplicate-submission and collision-retry policies.
 *
 * Depends on {@link UrlRepository} (interface, not SqliteUrlRepository)
 * so an in-memory fake can be injected in unit tests.
 *
 * TODO (decide and document in your report):
 *   - Duplicate long URL policy: return the existing short code, or mint a
 *     new one? Pick one and write the first test against it.
 */

public class ShortenerService {
}
