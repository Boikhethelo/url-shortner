package model;

import java.time.Instant;

/**
 * Immutable representation of a stored short-URL mapping.
 *
 * @param id         auto-incrementing database id (input to Base62Encoder)
 * @param shortCode  base62-encoded (or random) short code
 * @param longUrl    the original, validated URL
 * @param createdAt  creation timestamp
 * @param clickCount number of times the short code has been resolved
 */

public record ShortUrl(
        long id ,
        String shortCode,
        String longUrl,
        Instant createdAt,
        long clickCount
) {
}
