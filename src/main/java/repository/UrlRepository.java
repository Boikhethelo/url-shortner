package repository;

import model.ShortUrl;

import java.util.Optional;

/**
 * Persistence boundary for short URLs. ShortenerService depends on this
 * interface, not on SqliteUrlRepository directly, so an in-memory fake can
 * stand in during unit tests (Maintainability / Modularity evidence).
 */
public interface UrlRepository {

    /**
     * Persists a new mapping and returns it with its generated id.
     */

    ShortUrl save(ShortUrl shortUrl);

    /**
     * Looks up a mapping by its short code.
     */

    Optional<ShortUrl> findByCode(String shortCode);

    /**
     * Looks up an existing mapping by the original long URL — used to
     * implement the duplicate-submission policy.
     */

    Optional<ShortUrl> findByLongUrl(String longUrl);

    /**
     * Atomically increments the click counter for a short code.
     */

    void incrementClicks(String shortCode);

    /**
     * Sets the short code for an already-persisted row (used once the
     * generated id is known, to avoid inserting twice).
     */
    void updateShortCode(long id, String shortCode);




}
