package encoder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62EncoderTest {

    private final Base62Encoder encoder = new Base62Encoder();

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
        // TODO: assert encode(0) behaves sensibly and decode() round-trips it
    }

    @Test
    void encode_boundaryValue_longMax() {
        // TODO: assert encode(Long.MAX_VALUE) round-trips via decode()
    }

    @Test
    void encode_negativeId_throws() {
        // TODO: assertThrows(IllegalArgumentException.class, () -> encoder.encode(-1))
    }

    @Test
    void decode_invalidCharacter_throws() {
        // TODO: assertThrows(IllegalArgumentException.class, () -> encoder.decode("!!!"))
    }
}