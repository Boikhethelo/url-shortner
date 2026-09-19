package repository;

import model.ShortUrl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Optional;

/**
 * JDBC-backed {@link UrlRepository}. Uses PreparedStatement everywhere to
 * avoid SQL injection (doubles as Security evidence).
 */
public class SqliteUrlRepository implements UrlRepository{

    private final String jdbcUrl;

    public SqliteUrlRepository(String jdbcUrl){
        this.jdbcUrl = jdbcUrl;
        initSchema();
    }

    private void initSchema(){
        String ddl = """
                                CREATE TABLE IF NOT EXISTS short_urls (
                                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                                    short_code  TEXT NOT NULL UNIQUE,
                                    long_url    TEXT NOT NULL,
                                    created_at  TEXT NOT NULL,
                                    click_count INTEGER NOT NULL DEFAULT 0
                                    )
                """;

        try(Connection conn = getConnection();
                var stmt = conn.createStatement()){
            stmt.execute(ddl);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialise schema", e);
        }

    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    @Override
    public ShortUrl save(ShortUrl shortUrl) {
        // TODO: INSERT via PreparedStatement, return ShortUrl with generated id
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Optional<ShortUrl> findByCode(String shortCode) {
        // TODO: SELECT ... WHERE short_code = ?
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Optional<ShortUrl> findByLongUrl(String longUrl) {
        // TODO: SELECT ... WHERE long_url = ?
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void incrementClicks(String shortCode) {
        // TODO: UPDATE short_urls SET click_count = click_count + 1 WHERE short_code = ?
        throw new UnsupportedOperationException("not implemented");
    }



}
