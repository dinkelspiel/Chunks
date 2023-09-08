package dev.keii.chunks.commands;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class ClaimPower implements CommandExecutor {
    // claimpower [set,add,remove] <player> <number>
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length < 3)
        {
            sender.sendMessage(Component.text("Not enough arguments").color(NamedTextColor.RED));
            return true;
        }

        if(Bukkit.getServer().getPlayer(args[1]) == null)
        {
            sender.sendMessage(Component.text(args[1] + " is not a valid player").color(NamedTextColor.RED));
            return true;
        }

        try {
            Connection connection = Database.getConnection();
            assert connection != null;

            Statement userStatement = connection.createStatement();
            ResultSet userResultSet = userStatement.executeQuery("SELECT id FROM user WHERE uuid = '" + Bukkit.getServer().getPlayer(args[1]).getUniqueId().toString() + "'");
            userResultSet.next();
            long userId = userResultSet.getLong("id");
            userResultSet.close();
            userStatement.close();

            PreparedStatement statement;

            switch (args[0].toLowerCase()) {
                case "remove" -> {
                    statement = connection.prepareStatement("UPDATE user SET claim_power = claim_power - ? WHERE id = ?");
                    statement.setInt(1, Integer.parseInt(args[2]));
                    statement.setLong(2, userId);
                    statement.execute();
                }
                case "add" -> {
                    statement = connection.prepareStatement("UPDATE user SET claim_power = claim_power + ? WHERE id = ?");
                    statement.setInt(1, Integer.parseInt(args[2]));
                    statement.setLong(2, userId);
                    statement.execute();
                }
                case "set" -> {
                    statement = connection.prepareStatement("UPDATE user SET claim_power = ? WHERE id = ?");
                    statement.setInt(1, Integer.parseInt(args[2]));
                    statement.setLong(2, userId);
                    statement.execute();
                }
                default -> {
                    sender.sendMessage(Component.text("Argument 1 must be set, add, or remove"));
                }
            }

        } catch (SQLException e) {
            sender.sendMessage(Component.text("SQL Error '" + e.getMessage() + "'"));
        }  catch(NumberFormatException e) {
            sender.sendMessage(Component.text("Argument three isn't a number '" + args[2] + "'"));
        }

        sender.sendMessage(Component.text("Updated players claim power").color(NamedTextColor.GREEN));

        return true;
    }
}