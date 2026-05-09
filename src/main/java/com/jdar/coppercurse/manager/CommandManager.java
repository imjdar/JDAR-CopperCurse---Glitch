package com.jdar.coppercurse.manager;

import com.jdar.coppercurse.CopperCurse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles plugin commands.
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private final CopperCurse plugin;

    public CommandManager(CopperCurse plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("coppercurse.admin")) {
                sender.sendMessage("§cNo tienes permiso.");
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage("§aConfiguración recargada.");
            return true;
        }

        if (args[0].equalsIgnoreCase("toggle")) {
            if (!sender.hasPermission("coppercurse.admin")) {
                sender.sendMessage("§cNo tienes permiso.");
                return true;
            }
            boolean newState = !plugin.isPluginEnabled();
            plugin.setPluginEnabled(newState);
            sender.sendMessage("§eCopperCurse ha sido " + (newState ? "§aactivado" : "§cdesactivado") + "§e.");
            return true;
        }

        return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§lJDAR-CopperCurse Help");
        sender.sendMessage("§e/cc help §7- Muestra esta ayuda.");
        sender.sendMessage("§e/cc reload §7- Recarga la configuración.");
        sender.sendMessage("§e/cc toggle §7- Activa/Desactiva las mecánicas.");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("reload");
            completions.add("toggle");
        }
        return completions;
    }
}
