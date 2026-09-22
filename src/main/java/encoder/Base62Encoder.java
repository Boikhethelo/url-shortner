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
        // repeatedly divide by 62, map each remainder through the alphabet, then reverse.
        if (id < 0) throw new IllegalArgumentException("id must be >=0");
        if (id == 0) return String.valueOf(ALPHABET.charAt(0));
        StringBuilder sb = new StringBuilder();
        long n = id;
        while (n > 0){
            sb.append(ALPHABET.charAt((int) (n % BASE)));
            n /= BASE;
        }
        return sb.reverse().toString();

    }

    /**
     * @param code a base62 short code produced by {@link #encode(long)}
     * @return the original database id
     * @throws IllegalArgumentException if code contains characters outside the alphabet
     */

    public long decode(String code){
        if (code == null || code.isEmpty()){
            throw new IllegalArgumentException("code must not be null or empty");
        }

        long result = 0;
        for (int i = 0; i < code.length(); i++){
            char c = code.charAt(i);
            int digit = ALPHABET.indexOf(c);
            if (digit < 0){
                throw new IllegalArgumentException("code contains invalid character: " + c);
            }
            result = result * BASE + digit;
        }
        return result;
    }

}
