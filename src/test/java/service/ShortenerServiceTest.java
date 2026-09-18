package service;

import encoder.Base62Encoder;
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
    void shorten_sameLongUrlTwice_followsChosenDuplicatePolicy() {
        // TODO: decide the policy (return existing code vs. mint a new one),
        //       then assert it here. E.g. for "return existing":
        // ShortUrl first = service.shorten("https://example.com");
        // ShortUrl second = service.shorten("https://example.com");
        // assertEquals(first.shortCode(), second.shortCode());
    }

    @Test
    void resolve_unknownCode_throwsShortCodeNotFoundException() {
        // TODO: assertThrows(ShortCodeNotFoundException.class, () -> service.resolve("nope"))
    }

    @Test
    void resolve_knownCode_incrementsClickCount() {
        // TODO: shorten a URL, resolve it twice, assert clickCount reflects both resolves
    }
}