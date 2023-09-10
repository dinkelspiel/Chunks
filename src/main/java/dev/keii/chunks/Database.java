package dev.keii.chunks;

import dev.keii.chunks.database.Claim;
import dev.keii.chunks.database.ClaimPermission;
import dev.keii.chunks.database.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.File;
import java.sql.*;
import java.util.UUID;

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
    }

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

        try {
            Connection connection = Database.getConnection();
            assert connection != null;
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();

            ResultSet users = statement.executeQuery("SELECT * FROM user");
            Chunks.users.clear();
            while(users.next())
            {
                int id = users.getInt("id");
                String nickname = users.getString("nickname");
                String uuid = users.getString("uuid");
                Timestamp timestamp = users.getTimestamp("timestamp");
                int claim_power = users.getInt("claim_power");

                Chunks.users.add(new User(
                        id,
                        nickname,
                        UUID.fromString(uuid),
                        timestamp,
                        claim_power
                ));
            }
            users.close();

            ResultSet claims = statement.executeQuery("SELECT * FROM claim");
            Chunks.claims.clear();
            while(claims.next())
            {
                int id = claims.getInt("id");
                int userId = claims.getInt("user_id");
                int chunkX = claims.getInt("chunk_x");
                int chunkZ = claims.getInt("chunk_z");
                String world = claims.getString("world");
                Timestamp createdAt = claims.getTimestamp("created_at");
                Timestamp updatedAt = claims.getTimestamp("updated_at");
                boolean allowExplosions = claims.getBoolean("allow_explosions");

                Chunks.claims.add(new Claim(
                        id,
                        userId,
                        chunkX,
                        chunkZ,
                        world,
                        createdAt,
                        updatedAt,
                        allowExplosions
                ));
            }
            claims.close();

            ResultSet claimPermissions = statement.executeQuery("SELECT * FROM claim_permission");
            Chunks.claimPermissions.clear();
            while(claimPermissions.next())
            {
                int id = claimPermissions.getInt("id");
                int userId = claimPermissions.getInt("user_id");
                int claimId = claimPermissions.getInt("claim_id");
                boolean blockBreak = claimPermissions.getBoolean("block_break");
                boolean blockPlace = claimPermissions.getBoolean("block_place");
                boolean bucketEmpty = claimPermissions.getBoolean("bucket_empty");
                boolean bucketFill = claimPermissions.getBoolean("bucket_fill");
                boolean interact = claimPermissions.getBoolean("interact");
                Timestamp createdAt = claimPermissions.getTimestamp("created_at");
                Timestamp updatedAt = claimPermissions.getTimestamp("updated_at");

                Chunks.claimPermissions.add(new ClaimPermission(
                        id,
                        userId,
                        claimId,
                        blockBreak,
                        blockPlace,
                        bucketEmpty,
                        bucketFill,
                        interact,
                        createdAt,
                        updatedAt
                ));
            }
            claimPermissions.close();

            connection.commit();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            Chunks.sendMessageToStaff(
                    Component.text("Failed loading Chunks!").color(NamedTextColor.RED).appendNewline()
                            .append(Component.text("Got: " + e.getMessage()).color(NamedTextColor.RED))
            );
            return;
        }

        Chunks.sendMessageToStaff(Component.text("Finished loading Chunks!").color(NamedTextColor.GREEN));
    }

    public static void saveToDatabase()
    {
        Chunks.sendMessageToStaff(Component.text("Saving Chunks!").color(NamedTextColor.YELLOW));

        try {
            Connection connection = Database.getConnection();
            assert connection != null;
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.execute("DELETE * FROM user");
            statement.execute("DELETE * FROM claim");
            statement.execute("DELETE * FROM claim_permission");
            statement.close();

            PreparedStatement createUser = connection.prepareStatement("INSERT INTO user(id, nickname, uuid, timestamp, claim_power) VALUES(?, ?, ?, ?, ?)");
            for (User user : Chunks.users)
            {
                createUser.setInt(1, user.getId());
                createUser.setString(2, user.getNickname());
                createUser.setString(3, user.getUuid().toString());
                createUser.setTimestamp(4, user.getTimestamp());
                createUser.setInt(5, user.getClaimPower());
                createUser.execute();
            }
            createUser.close();

            PreparedStatement createClaim = connection.prepareStatement("INSERT INTO user(id, user_id, chunk_x, chunk_z, world, created_at, updated_at, allow_explosions) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            for (Claim claim : Chunks.claims)
            {
                createClaim.setInt(1, claim.getId());
                createClaim.setInt(2, claim.getUserID());
                createClaim.setInt(3, claim.getChunkX());
                createClaim.setInt(4, claim.getChunkZ());
                createClaim.setString(5, claim.getWorld());
                createClaim.setTimestamp(6, claim.getCreatedAt());
                createClaim.setTimestamp(7, claim.getUpdatedAt());
                createClaim.setBoolean(8, claim.getAllowExplosions());

                createClaim.execute();
            }
            createClaim.close();

            PreparedStatement createClaimPermission = connection.prepareStatement("INSERT INTO user(id, user_id, claim_id, block_break, block_place, bucket_empty, bucket_fill, interact, created_at, updated_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (ClaimPermission claimPermission : Chunks.claimPermissions)
            {
                createClaimPermission.setInt(1, claimPermission.getId());
                createClaimPermission.setInt(2, claimPermission.getUserId());
                createClaimPermission.setInt(3, claimPermission.getClaimId());
                createClaimPermission.setBoolean(4, claimPermission.getBlockBreak());
                createClaimPermission.setBoolean(5, claimPermission.getBlockPlace());
                createClaimPermission.setBoolean(6, claimPermission.getBucketEmpty());
                createClaimPermission.setBoolean(7, claimPermission.getBucketFill());
                createClaimPermission.setBoolean(8, claimPermission.getInteract());
                createClaimPermission.setTimestamp(9, claimPermission.getCreatedAt());
                createClaimPermission.setTimestamp(10, claimPermission.getUpdatedAt());

                createClaimPermission.execute();
            }
            createClaimPermission.close();

            connection.commit();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            Chunks.sendMessageToStaff(
                    Component.text("Failed saving Chunks!").color(NamedTextColor.RED).appendNewline()
                            .append(Component.text("Got: " + e.getMessage()).color(NamedTextColor.RED))
            );
            return;
        }

        Chunks.sendMessageToStaff(Component.text("Finished saving Chunks!").color(NamedTextColor.GREEN));
    }
}