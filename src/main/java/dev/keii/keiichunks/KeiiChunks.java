package dev.keii.keiichunks;

import dev.keii.keiichunks.commands.CommandMap;
import dev.keii.keiichunks.events.*;
import dev.keii.keiichunks.saveload.Claim;
import dev.keii.keiichunks.saveload.ClaimPermission;
import dev.keii.keiichunks.saveload.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Vector2i;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.util.Map;

public final class KeiiChunks extends JavaPlugin {

    private static KeiiChunks instance;
    public static List<RuntimeError> RuntimeErrors = new ArrayList<>();

    public static Map<Vector2i, Claim> claims = new HashMap<>();
    public static Map<String, User> users = new HashMap<>();
    public static Map<Long, ClaimPermission> claimPermissions = new HashMap<>();

    public static Config config;

    @Override
    public void onEnable() {
        instance = this;

        config = new Config();
        config.loadConfig();

        File pluginDir = new File("./plugins/KeiiChunks");
        if (!pluginDir.exists()){
            if(!pluginDir.mkdirs()) {
                Bukkit.getServer().sendMessage(Component.text("Creating plugin folders failed").color(NamedTextColor.RED));
            }
        }
        File sqlFile = new File("./plugins/KeiiChunks/database.sql");
        try {
            if (!sqlFile.createNewFile()){
                Bukkit.getServer().sendMessage(Component.text("Creating sql file failed").color(NamedTextColor.RED));
            } else {
                FileWriter myWriter = new FileWriter("./plugins/KeiiChunks/database.sql");
                myWriter.write(DatabaseConnector.createDatabaseSqlMySQL);
                myWriter.close();
                Bukkit.getServer().sendMessage(Component.text("Created sql file").color(NamedTextColor.YELLOW));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DatabaseConnector.InitializeDatabase();

        registerEvents();
        registerCommands();
    }

    public void registerEvents() {
        //This first line is optional, makes it faster with lots of classes
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerQuit(), this);
        pm.registerEvents(new BlockBreak(), this);
        pm.registerEvents(new BlockPlace(), this);
        pm.registerEvents(new BucketEmpty(), this);
        pm.registerEvents(new BucketFill(), this);
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new PlayerMove(), this);
        pm.registerEvents(new EntityExplode(), this);
        pm.registerEvents(new PlayerChat(), this);
        pm.registerEvents(new PlayerResourcePack(), this);
    }

    public void registerCommands() {
        this.getCommand("map").setExecutor(new CommandMap());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static KeiiChunks getInstance()
    {
        return instance;
    }

    public static void SaveData()
    {
        Connection connection = DatabaseConnector.getConnection();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            Bukkit.getServer().broadcast(Component.text("Server is saving! Expect some lag.").color(NamedTextColor.YELLOW));

            statement.execute("DELETE FROM user;");
            Bukkit.getServer().sendMessage(Component.text("Saving: Deleted users in database").color(NamedTextColor.YELLOW));

            for(User user : users.values())
            {
                statement.execute(String.format("INSERT INTO user(id, uuid, timestamp, claim_power) VALUES(%d, %s, %s, %d)", user.id, user.uuid, user.timestamp, user.claimPower));
            }
            Bukkit.getServer().sendMessage(Component.text("Saving: Saved new users into database").color(NamedTextColor.YELLOW));

            statement.execute("DELETE FROM claim;");
            Bukkit.getServer().sendMessage(Component.text("Saving: Deleted claims in database").color(NamedTextColor.YELLOW));

            for(Claim claim : claims.values())
            {
                statement.execute(String.format("INSERT INTO claim(id, user_id, chunk_x, chunk_z, created_at, updated_at, allow_explosions) VALUES(%d, %d, %d, %d, %s, %s, %d)", claim.id, claim.userId, claim.chunkX, claim.chunkZ, claim.createdAt, claim.updatedAt, claim.allowExplosions ? 1 : 0));
            }
            Bukkit.getServer().sendMessage(Component.text("Saving: Saved new claims into database").color(NamedTextColor.YELLOW));

            statement.execute("DELETE FROM claim_permission;");
            Bukkit.getServer().sendMessage(Component.text("Saving: Delete claim permissions in database").color(NamedTextColor.YELLOW));

            for(ClaimPermission claimPermission : claimPermissions.values())
            {
                statement.execute(String.format("INSERT INTO claim_permission(id, user_id, claim_id, block_break, block_place, bucket_empty, bucket_fill, interact, created_at, updated_at) VALUES(%d, %d, %d, %d, %d, %d, %d, %d, %s, %s)", claimPermission.id, claimPermission.userId, claimPermission.claimId, claimPermission.blockBreak ? 1 : 0, claimPermission.blockPlace ? 1 : 0, claimPermission.bucketEmpty ? 1 : 0, claimPermission.bucketFill ? 1 : 0, claimPermission.interact ? 1 : 0, claimPermission.createdAt, claimPermission.updatedAt));
            }
            Bukkit.getServer().sendMessage(Component.text("Saving: Saved new claim permissions into database").color(NamedTextColor.YELLOW));

            Bukkit.getServer().broadcast(Component.text("Save complete!").color(NamedTextColor.GREEN));

            connection.commit();
            statement.close();
            connection.close();
            return;
        } catch (SQLException e) {
            Bukkit.getServer().broadcast(Component.text("Save failed!").color(NamedTextColor.RED));
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            Bukkit.getServer().sendMessage(Component.text(e.getMessage()).color(NamedTextColor.RED));
            e.printStackTrace();
        }
    }

    public static void LoadData()
    {
        Connection connection = DatabaseConnector.getConnection();
        try {
            Bukkit.getServer().broadcast(Component.text("Server is saving! Expect some lag.").color(NamedTextColor.YELLOW));

            Statement statement = connection.createStatement();

            ResultSet users = statement.executeQuery("SELECT * FROM user;");

            while(users.next())
            {
                long id = users.getLong("id");
                String uuid = users.getString("uuid");
                String timestamp = users.getString("timestamp");
                int claimPower = users.getInt("claimPower");

                KeiiChunks.users.put(uuid, new User(id, uuid, timestamp, claimPower));
            }

            users.close();
            Bukkit.getServer().sendMessage(Component.text("Loading: Loaded users").color(NamedTextColor.YELLOW));

            ResultSet claims = statement.executeQuery("SELECT * FROM claim;");

            while(claims.next())
            {
                long id = claims.getLong("id");
                long userId = claims.getLong("user_id");
                long chunkX = claims.getLong("chunk_x");
                long chunkZ = claims.getLong("chunk_z");
                String createdAt = claims.getString("created_at");
                String updatedAt = claims.getString("updated_at");
                boolean allowExplosions = claims.getBoolean("allow_explosions");

                KeiiChunks.claims.put(new Vector2i((int) chunkX, (int) chunkZ), new Claim(id, userId, chunkX, chunkZ, createdAt, updatedAt, allowExplosions));
            }

            claims.close();
            Bukkit.getServer().sendMessage(Component.text("Loading: Loaded claims").color(NamedTextColor.YELLOW));

            ResultSet claimPermission = statement.executeQuery("SELECT * FROM claim_permission;");

            while(claimPermission.next())
            {
                long id = claims.getLong("id");
                long userId = claims.getLong("user_id");
                long claimId = claims.getLong("chunk_x");
                boolean blockBreak = claims.getBoolean("block_break");
                boolean blockPlace = claims.getBoolean("block_place");
                boolean bucketEmpty = claims.getBoolean("bucket_empty");
                boolean bucketFill = claims.getBoolean("bucket_fill");
                boolean interact = claims.getBoolean("interact");
                String createdAt = claims.getString("created_at");
                String updatedAt = claims.getString("updated_at");

                KeiiChunks.claimPermissions.put(claimId, new ClaimPermission(id, userId, claimId, blockBreak, blockPlace, bucketEmpty, bucketFill, interact, createdAt, updatedAt));
            }

            claimPermission.close();
            Bukkit.getServer().sendMessage(Component.text("Loading: Loaded claim permissions").color(NamedTextColor.YELLOW));

            Bukkit.getServer().broadcast(Component.text("Load complete!").color(NamedTextColor.GREEN));
            statement.close();
            connection.close();
        } catch (SQLException e) {
            Bukkit.getServer().broadcast(Component.text("Save failed!").color(NamedTextColor.RED));
            Bukkit.getServer().broadcast(Component.text(e.getMessage()).color(NamedTextColor.RED));
            e.printStackTrace();
        }
    }
}
