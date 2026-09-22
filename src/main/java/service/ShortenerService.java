package service;

import encoder.Base62Encoder;
import exception.ShortCodeNotFoundException;
import model.ShortUrl;
import repository.UrlRepository;
import validation.UrlValidator;

import java.time.Instant;

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

    private final UrlRepository repository;
    private final UrlValidator validator;
    private final Base62Encoder encoder;

    public ShortenerService(UrlRepository repository, UrlValidator validator, Base62Encoder encoder){
        this.repository = repository;
        this.validator = validator;
        this.encoder = encoder;
    }

    /**
     * Validates {@code longUrl}, then either returns an existing short code
     * for it or mints a new one, depending on the chosen duplicate policy.
     */

    public ShortUrl shorten(String longUrl) {
        validator.validate(longUrl);

        return repository.findByLongUrl(longUrl)
                .orElseGet(() -> createNewShortUrl(longUrl));
    }

    private ShortUrl createNewShortUrl(String longUrl) {
        ShortUrl toSave = new ShortUrl(0, null, longUrl, Instant.now(), 0);
        ShortUrl saved = repository.save(toSave);

        String encoded = encoder.encode(saved.id());
        repository.updateShortCode(saved.id(), encoded);

        return new ShortUrl(saved.id(), encoded, longUrl, saved.createdAt(), saved.clickCount());
    }

    /**
     * @throws ShortCodeNotFoundException if the code has no mapping
     */
    public ShortUrl resolve(String shortCode) {

        ShortUrl found = repository.findByCode(shortCode)
                .orElseThrow(() -> new ShortCodeNotFoundException("No mapping for code: " + shortCode));
        repository.incrementClicks(shortCode);
        return new ShortUrl(found.id(), found.shortCode(), found.longUrl(), found.createdAt(), found.clickCount() + 1);
    }


}
