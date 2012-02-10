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

    public void enableVanish(Player player, boolean force) {
        if (invisible.add(player.getName()) || force) {
            for (Player other : getServer().getOnlinePlayers()) {
                if (other.hasPermission("simplevanish.showvanished")) {
                    continue;
                }
                other.hidePlayer(player);
            }

            player.sendMessage(ChatColor.RED + "Poof!");
        } else {
            player.sendMessage(ChatColor.RED + "You are already vanished!");
        }
    }

    public void disableVanish(Player player, boolean force) {
        if (invisible.remove(player.getName()) || force) {
            for (Player other : getServer().getOnlinePlayers()) {
                other.showPlayer(player);
            }

            player.sendMessage(ChatColor.RED + "You have reappeared!");
        } else {
            player.sendMessage(ChatColor.RED + "You are not vanished!");
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        invisible = getConfig().getStringList("vanished");
    }

    @Override
    public void onDisable() {
        getConfig().set("vanished", invisible);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (!(sender instanceof Player))
            return false;

        if (command.getName().equalsIgnoreCase("vanish"))
            enableVanish((Player) sender, false);
        else if (command.getName().equalsIgnoreCase("unvanish"))
            disableVanish((Player) sender, false);

        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (invisible.contains(event.getPlayer().getName()))
            enableVanish(event.getPlayer(), true);

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
