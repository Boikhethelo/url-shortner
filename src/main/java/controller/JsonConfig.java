
package controller;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.json.JavalinJackson;

public final class JsonConfig {
    private JsonConfig() {}

    public static JavalinJackson mapper() {
        return new JavalinJackson().updateMapper(m -> {
            m.registerModule(new JavaTimeModule());
            m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        });
    }
}