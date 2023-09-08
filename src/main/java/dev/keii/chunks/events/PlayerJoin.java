package dev.keii.chunks.events;

import dev.keii.chunks.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        String resourcePackURL = "https://github.com/shykeiichi/plugin-resourcepack/raw/main/release.zip";
        player.setResourcePack(resourcePackURL);

        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String userQuery = "SELECT id FROM user WHERE uuid = \"" + player.getUniqueId() + "\"";
            ResultSet userResultSet = statement.executeQuery(userQuery);

            if (!userResultSet.next()) { // User does not exist
                userResultSet.close();
                String createUserQuery = "INSERT INTO user (uuid, nickname, claim_power) VALUES (\"" + player.getUniqueId() + "\", \"" + player.getName() + "\", 15)";
                statement.execute(createUserQuery);

                statement.close();
                connection.close();
                return;
            }

            userResultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            Bukkit.getServer().broadcast(Component.text("Fatal Database Error: " + e.getMessage()).color(NamedTextColor.RED));
        }
    }
}
