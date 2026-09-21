package validation;

import exception.InvalidUrlException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Rejects malformed URLs and dangerous schemes before they are ever stored.
 * This is the project's primary Security evidence — see the OWASP
 * Unvalidated Redirects and Forwards Cheat Sheet in the project guide.
 *
 * Uses {@link URI} parsing rather than regex to check scheme/host validity.
 */

public class UrlValidator {

    private static final Set<String> ALLOWED_SCHEMES = Set.of("http","https");

    /**
     * @param candidate a raw URL string supplied by a client
     * @throws InvalidUrlException if the URL is malformed, empty, or uses a
     *                              scheme outside {@link #ALLOWED_SCHEMES}
     *                              (e.g. javascript:, file:)
     */

    public void validate(String candidate){

        if(candidate == null || candidate.isBlank()){
            throw new InvalidUrlException("url cannot be null or blank");
        }

        URI parsed;

        try{
            parsed = parse(candidate);
        }catch (URISyntaxException e){
            throw new InvalidUrlException("Malformed URL: ");
        }

        if(!ALLOWED_SCHEMES.contains(parsed.getScheme())){
            throw new InvalidUrlException("Unallowed scheme");
        }

        if (parsed.getHost() == null){
            throw new InvalidUrlException("No path");
        }

    }

    private URI parse(String candidate) throws URISyntaxException {
        return new URI(candidate);
    }
}
