package dev.keii.chunks.tabcomplete;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleteClaimPower implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1)
        {
            return Arrays.asList("set", "add", "remove");
        } else if(args.length == 2)
        {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        } else if(args.length == 3)
        {
            return Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        }
        return new ArrayList<>();
    }
}
