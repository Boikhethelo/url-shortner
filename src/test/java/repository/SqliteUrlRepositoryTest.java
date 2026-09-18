package repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        // TODO: repository.save(...), then repository.findByCode(...), assert equality
    }

    @Test
    void findByCode_unknownCode_returnsEmpty() {
        // TODO: assertTrue(repository.findByCode("missing").isEmpty())
    }

    @Test
    void save_duplicateShortCode_violatesUniqueConstraint() {
        // TODO: assert the schema's UNIQUE constraint on short_code is enforced
    }

    @Test
    void incrementClicks_incrementsStoredCount() {
        // TODO: save, incrementClicks twice, findByCode, assert clickCount == 2
    }
}