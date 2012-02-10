package com.probablycoding.bukkit.simplevanish;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleVanish extends JavaPlugin implements Listener {
    public List<String> invisible;

    public void enableVanish(Player player) {
        for (Player other : getServer().getOnlinePlayers()) {
            if (other.hasPermission("simplevanish.showvanished")) {
                continue;
            }
            other.hidePlayer(player);
        }

        player.sendMessage(ChatColor.RED + "Poof!");
    }

    public void disableVanish(Player player) {
        if (invisible.remove(player.getName())) {
            for (Player other : getServer().getOnlinePlayers()) {
                other.showPlayer(player);
            }

            player.sendMessage(ChatColor.RED + "You have reappeared!");
        } else {
            player.sendMessage(ChatColor.RED + "You are not vanished!");
        }
    }

    public void showVanishList(Player player) {
        String result = "";
        boolean first = true;
        for (String hidden : invisible) {
            if (getServer().getPlayerExact(hidden) == null)
                continue;

            if (first) {
                result += hidden;
                first = false;
                continue;
            }

            result += ", " + hidden;
        }

        if (result.length() == 0)
            player.sendMessage(ChatColor.RED + "All players are visible!");
        else
            player.sendMessage(ChatColor.RED + "Vanished players: " + result);
    }

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        invisible = getConfig().getStringList("vanished");
    }

    public void onDisable() {
        getConfig().set("vanished", invisible);
        saveConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("vanish")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                showVanishList(player);
            }
            else {
                if (invisible.contains(player.getName())) {
                    player.sendMessage(ChatColor.RED + "You are already vanished!");
                }
                else {
                    invisible.add(player.getName());
                    enableVanish(player);
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("unvanish")) {
            disableVanish((Player) sender);
        }

        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (invisible.contains(event.getPlayer().getName()))
            enableVanish(event.getPlayer());

        if (event.getPlayer().hasPermission("simplevanish.showvanished"))
            return;

        for (String hidden : invisible) {
            Player hiddenPlayer = getServer().getPlayerExact(hidden);
            if (hiddenPlayer != null) {
                event.getPlayer().hidePlayer(hiddenPlayer);
            }
        }
    }
}
