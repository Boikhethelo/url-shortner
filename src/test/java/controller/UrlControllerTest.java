package controller;

import encoder.Base62Encoder;
import io.javalin.Javalin;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.FakeUrlRepository;
import repository.UrlRepository;
import service.ShortenerService;
import validation.UrlValidator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UrlControllerTest {

    private Javalin app;
    private ShortenerService service;
    private final OkHttpClient noRedirectClient = new OkHttpClient.Builder()
            .followRedirects(false)
            .build();

    @BeforeEach
    void setUp() {
        UrlRepository repository = new FakeUrlRepository();
        service = new ShortenerService(repository, new UrlValidator(), new Base62Encoder());
        app = Javalin.create();
        new UrlController(service).registerRoutes(app);
        app.start(0); // random free port
    }

    @AfterEach
    void tearDown() {
        app.stop();
    }

    @Test
    void getKnownCode_returns302WithLocationHeader() throws IOException {
        String code = service.shorten("https://example.com").shortCode();

        Request request = new Request.Builder()
                .url("http://localhost:" + app.port() + "/" + code)
                .build();

        try (var response = noRedirectClient.newCall(request).execute()) {
            assertEquals(302, response.code());
            assertEquals("https://example.com", response.header("Location"));
        }
    }
}