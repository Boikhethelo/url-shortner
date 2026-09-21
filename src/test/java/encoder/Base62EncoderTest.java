package encoder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62EncoderTest {

    private final Base62Encoder encoder = new Base62Encoder();
    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Test
    void encodeThenDecode_returnsOriginalId() {
        // Arrange
        long id = 12345L;
        // Act
        String code = encoder.encode(id);
        long decoded = encoder.decode(code);
        // Assert
        assertEquals(id, decoded);
    }

    @Test
    void encode_boundaryValue_zero() {
        String code = encoder.encode(0);
        assertEquals(String.valueOf(ALPHABET.charAt(0)),code);
        assertEquals(0L,encoder.decode(code));

    }

    @Test
    void encode_boundaryValue_longMax() {
        String code = encoder.encode(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE,encoder.decode(code));
    }

    @Test
    void encode_negativeId_throws() {
        assertThrows(IllegalArgumentException.class, () -> encoder.encode(-1));
    }

    @Test
    void decode_invalidCharacter_throws() {
        assertThrows(IllegalArgumentException.class, () -> encoder.decode("!!!"));
    }
}