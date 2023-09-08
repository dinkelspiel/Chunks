package dev.keii.chunks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public boolean Claims;
    public boolean Invite;

    public String ApiUrl;

    public String DbUrl;
    public String DbName;
    public String DbUser;
    public String DbPassword;


    public void fileConfigToConfig(FileConfiguration config)
    {
        DbUrl = config.getString("dbUrl");
        DbName = config.getString("dbName");
        DbUser = config.getString("dbUser");
        DbPassword = config.getString("dbPassword");
    }

    public void loadConfig() {
        FileConfiguration config = Chunks.getInstance().getConfig();

        if(config.getString("dbUrl") == null)
            config.set("dbUrl", "jdbc:mysql://localhost:3306/");
        if(config.getString("dbName") == null)
            config.set("dbName", "");
        if(config.getString("dbUser") == null)
            config.set("dbUser", "");
        if(config.getString("dbPassword") == null)
            config.set("dbPassword", "");

        config.options().copyDefaults(true);
        Chunks.getInstance().saveConfig();

        config = Chunks.getInstance().getConfig();

        if(config.getString("dbName").isEmpty())
        {
            Bukkit.broadcast(Component.text("Database is not set in the config. Core functionality of the plugin ").color(NamedTextColor.RED)
                    .append(
                            Component.text("will not")
                                    .decorate(TextDecoration.ITALIC)
                                    .decorate(TextDecoration.BOLD))
                    .color(NamedTextColor.RED)
                    .append(
                            Component.text(" work")
                                    .color(NamedTextColor.RED)));
        }

        fileConfigToConfig(config);

        Database.DB_URL = DbUrl;
        Database.DB_NAME = DbName;
        Database.DB_USER = DbUser;
        Database.DB_PASSWORD = DbPassword;
    }
}
