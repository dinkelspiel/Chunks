package dev.keii.barter.commands;

import dev.keii.chunks.Chunks;
import dev.keii.chunks.Config;
import dev.keii.chunks.Database;
import dev.keii.chunks.Responses;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class CommandChunks implements CommandExecutor {
    private void sendInfo(CommandSender sender)
    {
        sender.sendMessage(Component.text("Keii's Chunks").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Version: " + Chunks.getInstance().getDescription().getVersion()).color(NamedTextColor.YELLOW));
    }

    private void sendHelp(CommandSender sender)
    {
        sender.sendMessage(
                Component.text("")
                        .append(Component.text("/chunks").color(NamedTextColor.GOLD))
                        .appendNewline()
                        .append(Component.text("    info - Get info about the plugin").color(NamedTextColor.YELLOW))
                        .appendNewline()
                        .append(Component.text("    help - Get help for chunks").color(NamedTextColor.YELLOW))
                        .appendNewline()
                        .append(Component.text("    reload - Reload chunks config").color(NamedTextColor.YELLOW))
                        .appendNewline()
                        .append(Component.text("Staff:").color(NamedTextColor.YELLOW))
                        .appendNewline()
                        .append(Component.text("/claimpower [set,add,remove] <player> <amount> - Modify claimpower of user").color(NamedTextColor.GOLD))
                        .appendNewline()
                        .append(Component.text("/chunkoverride <player?> - Override chunkpermissions").color(NamedTextColor.GOLD))
        );
    }

    private void reload(CommandSender sender)
    {
        if(!sender.hasPermission("keii.chunks.reload"))
        {
            sender.sendMessage(Responses.noPermissionResponse);
            return;
        }

        Chunks.config = new Config();
        Chunks.config.loadConfig();

        Chunks.sendMessageToStaff(Component.text("Reloaded Chunks config!").color(NamedTextColor.YELLOW));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1)
        {
            sendHelp(sender);
            return true;
        }

        switch(args[0])
        {
            case "info":
                sendInfo(sender);
                break;
            case "reload":
                reload(sender);
                break;
            case "help":
            default:
                sendHelp(sender);
        }

        return true;
    }
}
