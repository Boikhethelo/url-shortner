package repository;

/**
 * Persistence boundary for short URLs. ShortenerService depends on this
 * interface, not on SqliteUrlRepository directly, so an in-memory fake can
 * stand in during unit tests (Maintainability / Modularity evidence).
 */
public interface UrlRepository {


}
