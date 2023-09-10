package dev.keii.chunks;

import dev.keii.chunks.saveload.Claim;
import dev.keii.chunks.saveload.ClaimPermission;
import dev.keii.chunks.saveload.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.joml.Vector2i;
import java.io.File;

import java.sql.*;

public class Database {
    private static final String createClaimTableSQL =
            """
            CREATE TABLE IF NOT EXISTS claim (
              id INTEGER PRIMARY KEY,
              user_id INTEGER NOT NULL,
              chunk_x INTEGER NOT NULL,
              chunk_z INTEGER NOT NULL,
              world TEXT NOT NULL,
              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              allow_explosions INTEGER NOT NULL DEFAULT 0
            );
    """;
    private static final String createClaimPermissionTableSQL = """
            CREATE TABLE IF NOT EXISTS claim_permission (
              id INTEGER PRIMARY KEY,
              user_id INTEGER,
              claim_id INTEGER NOT NULL,
              block_break INTEGER NOT NULL DEFAULT 0,
              block_place INTEGER NOT NULL DEFAULT 0,
              bucket_empty INTEGER NOT NULL DEFAULT 0,
              bucket_fill INTEGER NOT NULL DEFAULT 0,
              interact INTEGER NOT NULL DEFAULT 0,
              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            );
    """;
    private static final String createUserTableSQL = """
            CREATE TABLE IF NOT EXISTS user (
              id INTEGER PRIMARY KEY,
              nickname TEXT NOT NULL,
              uuid TEXT NOT NULL,
              timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              claim_power INTEGER NOT NULL DEFAULT 10
            );
    """;

    public static void initializeDatabase()
    {
        try {
            Connection connection = getConnection();
            assert connection != null;
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();

            statement.execute(createClaimTableSQL);
            statement.execute(createClaimPermissionTableSQL);
            statement.execute(createUserTableSQL);

            Chunks.sendMessageToStaff(Component.text("Database initialization succeeded").color(NamedTextColor.GREEN));

            connection.commit();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            Chunks.sendMessageToStaff(Component.text("Failed initializing database: " + e.getMessage()).color(NamedTextColor.RED));
        }
    };

    public static Connection getConnection() {
        try {
            File f = new File("./plugins/Chunks");
            f.mkdir();

            final String url = "jdbc:sqlite:./plugins/Chunks/database.db";

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            Chunks.sendMessageToStaff(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
            return null;
        }
    }

    public static void loadFromDatabase()
    {
        Chunks.sendMessageToStaff(Component.text("Loading Chunks!").color(NamedTextColor.YELLOW));

        Connection connection = null;

        try {
            connection = Database.getConnection();
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();

            ResultSet users = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveToDatabase()
    {

    }
}