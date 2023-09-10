package dev.keii.chunks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public String dbUrl;
    public String dbName;
    public String dbUser;
    public String dbPassword;


    public void fileConfigToConfig(FileConfiguration config)
    {
        dbUrl = config.getString("dbUrl");
        dbName = config.getString("dbName");
        dbUser = config.getString("dbUser");
        dbPassword = config.getString("dbPassword");
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
    }
}
