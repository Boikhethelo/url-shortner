package service;

import encoder.Base62Encoder;
import exception.ShortCodeNotFoundException;
import model.ShortUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.FakeUrlRepository;
import repository.UrlRepository;
import validation.UrlValidator;

import static org.junit.jupiter.api.Assertions.*;

class ShortenerServiceTest {

    private UrlRepository repository;
    private ShortenerService service;

    @BeforeEach
    void setUp() {
        repository = new FakeUrlRepository();
        service = new ShortenerService(repository, new UrlValidator(), new Base62Encoder());
    }

    @Test
    void shorten_sameLongUrlTwice_returnsExistingShortCode() {
        ShortUrl first = service.shorten("https://example.com");
        ShortUrl second = service.shorten("https://example.com");

        assertEquals(first.shortCode(), second.shortCode());
        assertEquals(first.id(), second.id());
    }

    @Test
    void resolve_unknownCode_throwsShortCodeNotFoundException() {
        assertThrows(ShortCodeNotFoundException.class, () -> service.resolve("nope"));
    }

    @Test
    void resolve_knownCode_incrementsClickCount() {
        ShortUrl shortened = service.shorten("https://example.com");

        service.resolve(shortened.shortCode());
        ShortUrl afterSecondResolve = service.resolve(shortened.shortCode());

        assertEquals(2, afterSecondResolve.clickCount());
    }
}