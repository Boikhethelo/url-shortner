package validation;

import exception.InvalidUrlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {

    private final UrlValidator validator = new UrlValidator();

    @ParameterizedTest
    @ValueSource(strings = {"http://example.com", "https://example.com/path?x=1"})
    void validate_acceptsHttpAndHttps(String url) {
        assertDoesNotThrow(() -> validator.validate(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "javascript:alert(1)",
            "file:///etc/passwd",
            "not a url",
            ""
    })
    void validate_rejectsDisallowedOrMalformedInput(String url) {
        assertThrows(InvalidUrlException.class, () -> validator.validate(url));
    }

    @Test
    void validate_nullInput_throws() {
        assertThrows(InvalidUrlException.class, () -> validator.validate(null));
    }
}