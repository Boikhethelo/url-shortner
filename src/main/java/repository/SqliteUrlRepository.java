package repository;

import model.ShortUrl;

import java.sql.*;
import java.time.Instant;
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
                                    short_code  TEXT UNIQUE,
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
        String sql = "INSERT INTO short_urls(short_code, long_url, created_at, click_count) VALUES(?,?,?,?)";

        try(PreparedStatement stmt = getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1,shortUrl.shortCode());
            stmt.setString(2, shortUrl.longUrl());
            stmt.setLong(3,shortUrl.createdAt().toEpochMilli());
            stmt.setInt(4,(int) shortUrl.clickCount());
            stmt.executeUpdate();


            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("Insert failed, no generated key obtained for short_code: " + shortUrl.shortCode());
                }
                long id = generatedKeys.getLong(1);
                return new ShortUrl(id, shortUrl.shortCode(), shortUrl.longUrl(), shortUrl.createdAt(), shortUrl.clickCount());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save ShortUrl: " + shortUrl.shortCode(), e);
        }

        }


    @Override
    public Optional<ShortUrl> findByCode(String shortCode) {
        String sql = "SELECT * FROM short_urls WHERE short_code = ?";

        try(PreparedStatement stmt = getConnection().prepareStatement(sql)){
            stmt.setString(1,shortCode);

            try(ResultSet results = stmt.executeQuery()) {
                if (!results.next()) {
                    return Optional.empty();
                }

                ShortUrl output = new ShortUrl(
                        results.getLong("id"),
                        shortCode,
                        results.getString("long_url"),
                        Instant.ofEpochMilli(results.getLong("created_at")),
                        results.getInt("click_count")
                );

                return Optional.of(output);
            }

        }catch (SQLException e){
            throw new RuntimeException("", e);
        }
    }

    @Override
    public Optional<ShortUrl> findByLongUrl(String longUrl) {
        String sql = "SELECT * FROM short_urls WHERE long_url = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, longUrl);

            try (ResultSet results = stmt.executeQuery()) {
                if (!results.next()) {
                    return Optional.empty();
                }

                ShortUrl output = new ShortUrl(
                        results.getLong("id"),
                        results.getString("short_code"),
                        longUrl,
                        Instant.ofEpochMilli(results.getLong("created_at")),
                        results.getInt("click_count")
                );

                return Optional.of(output);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ShortUrl by long URL: " + longUrl, e);
        }
    }

    @Override
    public void incrementClicks(String shortCode) {
        String sql = "UPDATE short_urls SET click_count = click_count + 1 WHERE short_code = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, shortCode);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("No ShortUrl found with short code: " + shortCode);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to increment clicks for short code: " + shortCode, e);
        }
    }

    @Override
    public void updateShortCode(long id, String shortCode) {
        String sql = "UPDATE short_urls SET short_code = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, shortCode);
            stmt.setLong(2, id);
            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("No row found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update short code for id: " + id, e);
        }
    }



}
