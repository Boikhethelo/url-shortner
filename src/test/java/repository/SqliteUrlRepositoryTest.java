package repository;

import model.ShortUrl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SqliteUrlRepositoryTest {

    private File tempDbFile;
    private SqliteUrlRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        tempDbFile = Files.createTempFile("url-shortener-test", ".db").toFile();
        repository = new SqliteUrlRepository("jdbc:sqlite:" + tempDbFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        tempDbFile.delete();
    }

    @Test
    void save_thenFindByCode_returnsSameMapping() {
        ShortUrl toSave = new ShortUrl(0L, "abc123", "https://example.com", Instant.now(), 0);

        ShortUrl saved = repository.save(toSave);
        Optional<ShortUrl> found = repository.findByCode("abc123");

        assertTrue(found.isPresent());
        assertEquals(saved.id(), found.get().id());
        assertEquals("abc123", found.get().shortCode());
        assertEquals("https://example.com", found.get().longUrl());
        assertEquals(0, found.get().clickCount());
    }

    @Test
    void findByCode_unknownCode_returnsEmpty() {
        assertTrue(repository.findByCode("missing").isEmpty());
    }

    @Test
    void save_duplicateShortCode_violatesUniqueConstraint() {
        ShortUrl first = new ShortUrl(0L, "dup123", "https://example.com/first", Instant.now(), 0);
        ShortUrl second = new ShortUrl(0L, "dup123", "https://example.com/second", Instant.now(), 0);

        repository.save(first);

        assertThrows(RuntimeException.class, () -> repository.save(second));
    }

    @Test
    void incrementClicks_incrementsStoredCount() {
        ShortUrl toSave = new ShortUrl(0L, "click1", "https://example.com", Instant.now(), 0);
        repository.save(toSave);

        repository.incrementClicks("click1");
        repository.incrementClicks("click1");

        Optional<ShortUrl> found = repository.findByCode("click1");
        assertTrue(found.isPresent());
        assertEquals(2, found.get().clickCount());
    }

    @Test
    void findByLongUrl_knownUrl_returnsMapping() {
        ShortUrl toSave = new ShortUrl(0L, "byurl1", "https://example.com/byurl", Instant.now(), 0);
        repository.save(toSave);

        Optional<ShortUrl> found = repository.findByLongUrl("https://example.com/byurl");

        assertTrue(found.isPresent());
        assertEquals("byurl1", found.get().shortCode());
    }

    @Test
    void findByLongUrl_unknownUrl_returnsEmpty() {
        assertTrue(repository.findByLongUrl("https://nope.example.com").isEmpty());
    }

    @Test
    void updateShortCode_setsCodeOnExistingRow() {
        ShortUrl toSave = new ShortUrl(0L, null, "https://example.com/pending", Instant.now(), 0);
        ShortUrl saved = repository.save(toSave);

        repository.updateShortCode(saved.id(), "newcode1");

        assertTrue(repository.findByCode("newcode1").isPresent());
    }
}