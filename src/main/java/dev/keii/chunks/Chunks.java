package dev.keii.chunks;

import dev.keii.barter.commands.CommandChunks;
import dev.keii.chunks.commands.ChunkOverride;
import dev.keii.chunks.commands.ClaimPower;
import dev.keii.chunks.commands.CommandMap;
import dev.keii.chunks.models.Claim;
import dev.keii.chunks.models.ClaimPermission;
import dev.keii.chunks.models.User;
import dev.keii.chunks.events.*;
import dev.keii.chunks.tabcomplete.TabCompleteChunks;
import dev.keii.chunks.tabcomplete.TabCompleteClaimPower;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Chunks extends JavaPlugin {

    private static Chunks instance;
    public static Config config;

    public static List<User> users = new ArrayList<>();
    public static List<Claim> claims = new ArrayList<>();
    public static List<ClaimPermission> claimPermissions = new ArrayList<>();

    public static int usersAutoIncrement = 0;
    public static int claimsAutoIncrement = 0;
    public static int claimPermissionsAutoIncrement = 0;


    @Override
    public void onEnable() {
        instance = this;

        config = new Config();
        config.loadConfig();

        File pluginDir = new File("./plugins/Chunks");
        if (!pluginDir.exists()){
            if(!pluginDir.mkdirs()) {
                sendMessageToStaff(Component.text("Creating plugin folders failed").color(NamedTextColor.RED));
            }
        }

        Database.initializeDatabase();
        Database.loadFromDatabase();

        registerEvents();
        registerCommands();
    }

    public void registerEvents() {
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
            if(player.hasPermission("keii.chunks.staffmessage"))
            {
                player.sendMessage(text);
            }
        }
    }
}
