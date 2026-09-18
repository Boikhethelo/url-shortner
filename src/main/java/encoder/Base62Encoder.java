package encoder;

/**
 * Converts an auto-incrementing database id into a short, URL-safe,
 * base62-alphabet code, and back again.
 *
 * No database I/O — kept pure so mutation testing (PITest) on this
 * class stays fast.
 */

public class Base62Encoder {

    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();

    /**
     * @param id a non-negative database id
     * @return the base62 encoding of {@code id}
     * @throws IllegalArgumentException if id is negative
     */

    public String encode(long id){
        // TODO: implement base62 encoding (see Baeldung reference in the project guide)
        throw new UnsupportedOperationException("not implemented");

    }

    /**
     * @param code a base62 short code produced by {@link #encode(long)}
     * @return the original database id
     * @throws IllegalArgumentException if code contains characters outside the alphabet
     */

    public long decode(String code){
        // TODO: implement base62 decoding, inverse of encode()
        throw new UnsupportedOperationException("not implemented");
    }

}
