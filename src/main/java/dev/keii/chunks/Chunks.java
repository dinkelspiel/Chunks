package dev.keii.chunks;

import dev.keii.barter.commands.CommandChunks;
import dev.keii.chunks.commands.ChunkOverride;
import dev.keii.chunks.commands.ClaimPower;
import dev.keii.chunks.commands.CommandMap;
import dev.keii.chunks.events.*;
import dev.keii.chunks.saveload.Claim;
import dev.keii.chunks.saveload.ClaimPermission;
import dev.keii.chunks.saveload.User;
import dev.keii.chunks.tabcomplete.TabCompleteChunks;
import dev.keii.chunks.tabcomplete.TabCompleteClaimPower;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Vector2i;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.io.File;
import java.util.Map;

public final class Chunks extends JavaPlugin {

    private static Chunks instance;

    public static Config config;

    @Override
    public void onEnable() {
        instance = this;

        config = new Config();
        config.loadConfig();

        File pluginDir = new File("./plugins/Chunks");
        if (!pluginDir.exists()){
            if(!pluginDir.mkdirs()) {
                Bukkit.getServer().sendMessage(Component.text("Creating plugin folders failed").color(NamedTextColor.RED));
            }
        }
        File sqlFile = new File("./plugins/Chunks/database.sql");
        try {
            if (!sqlFile.createNewFile()){
                Bukkit.getServer().sendMessage(Component.text("Creating sql file failed").color(NamedTextColor.RED));
            } else {
                FileWriter myWriter = new FileWriter("./plugins/Chunks/database.sql");
                myWriter.write(Database.createDatabaseSQL);
                myWriter.close();
                Bukkit.getServer().sendMessage(Component.text("Created sql file").color(NamedTextColor.YELLOW));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Database.initializeDatabase();

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
        this.getCommand("chunks").setExecutor(new CommandChunks());
        this.getCommand("map").setExecutor(new CommandMap());
        this.getCommand("chunkoverride").setExecutor(new ChunkOverride());
        this.getCommand("claimpower").setExecutor(new ClaimPower());

        this.getCommand("chunks").setTabCompleter(new TabCompleteChunks());
        this.getCommand("claimpower").setTabCompleter(new TabCompleteClaimPower());
    }

    public static Chunks getInstance()
    {
        return instance;
    }

    public static void sendMessageToStaff(Component text)
    {
        Bukkit.getConsoleSender().sendMessage(text);
        for(Player player : Bukkit.getOnlinePlayers())
        {
            if(player.hasPermission("keii.chunks.staff.message"))
            {
                player.sendMessage(text);
            }
        }
    }
}
