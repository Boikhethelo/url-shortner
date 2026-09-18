package controller;

import encoder.Base62Encoder;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.FakeUrlRepository;
import repository.UrlRepository;
import service.ShortenerService;
import validation.UrlValidator;

class UrlControllerTest {

    private Javalin app;

    @BeforeEach
    void setUp() {
        UrlRepository repository = new FakeUrlRepository();
        ShortenerService service = new ShortenerService(repository, new UrlValidator(), new Base62Encoder());
        app = Javalin.create();
        new UrlController(service).registerRoutes(app);
    }

    @Test
    void postShorten_validUrl_returns201WithShortUrl() {
        // TODO: JavalinTest.test(app, (server, client) -> { ... POST /api/shorten ... assert 201 });
    }

    @Test
    void getUnknownCode_returns404() {
        // TODO: JavalinTest.test(app, (server, client) -> { ... GET /nope ... assert 404 });
    }

    @Test
    void getKnownCode_returns302WithLocationHeader() {
        // TODO: shorten first, then GET the code, assert 302 + Location header == longUrl
    }
}