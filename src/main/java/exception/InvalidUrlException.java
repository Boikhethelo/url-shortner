package exception;

/**
 * Thrown by {@link validation.UrlValidator} when a candidate URL is
 * malformed, empty, or uses a disallowed scheme.
 */

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String message) {

        super(message);
    }
}
