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

public class TabCompleteChunks implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1)
        {
            return Arrays.asList("info", "help");
        }
        return new ArrayList<>();
    }
}
