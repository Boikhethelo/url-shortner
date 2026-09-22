package app;

import controller.JsonConfig;
import controller.UrlController;
import encoder.Base62Encoder;
import io.javalin.Javalin;
import repository.SqliteUrlRepository;
import repository.UrlRepository;
import service.ShortenerService;
import validation.UrlValidator;

/**
 * Wires everything together and starts Javalin on localhost.
 */

public class Main {

    public static void main(String[] args){
        UrlRepository repository = new SqliteUrlRepository("jdbc:sqlite:url-shortener.db");
        UrlValidator validator = new UrlValidator();
        Base62Encoder encoder = new Base62Encoder();
        ShortenerService service = new ShortenerService(repository, validator, encoder);
        UrlController controller = new UrlController(service);

        Javalin app = Javalin.create(config -> config.jsonMapper(JsonConfig.mapper()));
        controller.registerRoutes(app);
        app.start("localhost",7000);
    }
}
