package dev.keii.chunks.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChunkOverride implements CommandExecutor {
    private static final List<String> chunkOverridePlayers = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You must run this command as player!").color(NamedTextColor.RED));
            return false;
        }

        if(args.length > 0)
        {
            Player setPlayer = Bukkit.getServer().getPlayer(args[0]);

            if(setPlayer == null)
            {
                player.sendMessage(Component.text("Invalid player " + args[0]).color(NamedTextColor.RED));
                return true;
            }

            if(toggleChunkOverride(setPlayer))
            {
                player.sendMessage(Component.text("Turned on chunk override for " + setPlayer.getName()).color(NamedTextColor.YELLOW));
            } else {
                player.sendMessage(Component.text("Turned off chunk override for " + setPlayer.getName()).color(NamedTextColor.YELLOW));
            }

            return true;
        }

        toggleChunkOverride(player);

        return true;
    }

    public static boolean getChunkOverrideForPlayer(Player player)
    {
        return chunkOverridePlayers.contains(player.getUniqueId().toString());
    }

    private static boolean toggleChunkOverride(Player player)
    {
        if(chunkOverridePlayers.contains(player.getUniqueId().toString()))
        {
            chunkOverridePlayers.remove(player.getUniqueId().toString());
            player.sendMessage(Component.text("Turned off chunk override").color(NamedTextColor.YELLOW));
            return false;
        } else {
            chunkOverridePlayers.add(player.getUniqueId().toString());
            player.sendMessage(Component.text("Turned on chunk override").color(NamedTextColor.YELLOW));
            return true;
        }
    }
}