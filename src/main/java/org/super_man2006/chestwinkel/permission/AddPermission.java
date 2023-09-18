package org.super_man2006.chestwinkel.permission;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.super_man2006.chestwinkel.ChestWinkel;

import java.util.ArrayList;
import java.util.List;

public class AddPermission implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(Component.text("You are not allowed to use this command!").color(NamedTextColor.RED));
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("InfiniteChestPermission")) {
            if (args.length != 2) {
                sender.sendMessage(Component.text("/InfiniteChestPermission <add/remove> <player>").color(NamedTextColor.RED));
                return true;
            }

            if (args[0].toLowerCase().equals("add")) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    return true;
                }

                player.getPersistentDataContainer().set(new NamespacedKey(ChestWinkel.plugin, "InfiniteChestPermission"), PersistentDataType.BOOLEAN, true);
            } else if (args[1].toLowerCase().equals("remove")) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    return true;
                }

                player.getPersistentDataContainer().set(new NamespacedKey(ChestWinkel.plugin, "InfiniteChestPermission"), PersistentDataType.BOOLEAN, false);
            } else {
                sender.sendMessage(Component.text("/InfiniteChestPermission <add/remove> <player>").color(NamedTextColor.RED));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(cmd.getName().equalsIgnoreCase("InfiniteChestPermission"))) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            List<String> tab = new ArrayList<>();
            tab.add("add");
            tab.add("remove");
            return tab;
        }

        if (args.length == 2) {
            List<String> tab = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach( player -> tab.add(player.getName()));
            return tab;
        }

        return new ArrayList<>();
    }
}
