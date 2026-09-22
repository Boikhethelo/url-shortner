package repository;

import model.ShortUrl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory {@link UrlRepository} used in unit tests so ShortenerService
 * tests (and PITest mutation runs on the encoder/validator) don't touch
 * a real database.
 */
public class FakeUrlRepository implements UrlRepository {

    private final Map<String, ShortUrl> byCode = new LinkedHashMap<>();
    private final AtomicLong ids = new AtomicLong(1);

    @Override
    public ShortUrl save(ShortUrl shortUrl) {
        long id = ids.getAndIncrement();
        ShortUrl saved = new ShortUrl(id, shortUrl.shortCode(), shortUrl.longUrl(),
                shortUrl.createdAt(), shortUrl.clickCount());
        byCode.put(saved.shortCode(), saved);
        return saved;
    }

    @Override
    public Optional<ShortUrl> findByCode(String shortCode) {
        return Optional.ofNullable(byCode.get(shortCode));
    }

    @Override
    public Optional<ShortUrl> findByLongUrl(String longUrl) {
        return byCode.values().stream()
                .filter(u -> u.longUrl().equals(longUrl))
                .findFirst();
    }

    @Override
    public void incrementClicks(String shortCode) {
        ShortUrl existing = byCode.get(shortCode);
        if (existing != null) {
            byCode.put(shortCode, new ShortUrl(existing.id(), existing.shortCode(),
                    existing.longUrl(), existing.createdAt(), existing.clickCount() + 1));
        }
    }

    @Override
    public void updateShortCode(long id, String shortCode) {
        byCode.values().stream()
                .filter(u -> u.id() == id)
                .findFirst()
                .ifPresent(existing -> {
                    byCode.remove(existing.shortCode());
                    byCode.put(shortCode, new ShortUrl(existing.id(), shortCode,
                            existing.longUrl(), existing.createdAt(), existing.clickCount()));
                });
    }
}
