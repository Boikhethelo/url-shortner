package service;

import encoder.Base62Encoder;
import model.ShortUrl;
import repository.UrlRepository;
import validation.UrlValidator;

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
        // TODO:
        //  1. validator.validate(longUrl)
        //  2. check repository.findByLongUrl(longUrl) per duplicate policy
        //  3. repository.save(...) then encoder.encode(id) for the short code
        //     (or generate+check a random code if using collision-retry mode)
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * @throws ShortCodeNotFoundException if the code has no mapping
     */
    public ShortUrl resolve(String shortCode) {
        // TODO: repository.findByCode(shortCode), increment clicks, or throw
        throw new UnsupportedOperationException("not implemented");
    }


}
