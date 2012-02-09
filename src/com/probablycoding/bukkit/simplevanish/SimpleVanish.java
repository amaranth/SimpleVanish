package com.probablycoding.bukkit.simplevanish;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleVanish extends JavaPlugin {
    public static final Logger log = Logger.getLogger("Minecraft");
    public HashSet<Player> invisible = new HashSet<Player>();
    private SimpleVanishListener listener = new SimpleVanishListener(this);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(listener, this);
        sendToLog(Level.INFO, getDescription().getVersion() + " enabled");
    }

    @Override
    public void onDisable() {
        sendToLog(Level.INFO, getDescription().getVersion() + " disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        boolean result = false;

        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("vanish")) {
                enableVanish((Player) sender);
                result = true;
            }
            else if (command.getName().equalsIgnoreCase("unvanish")) {
                disableVanish((Player) sender);
                result = true;
            }
        }

        return result;
    }

    public void sendToLog(Level level, String message) {
        log.log(level, "[" + getDescription().getName() + "] " + message);
    }

    public void enableVanish(Player player) {
        for (Player other : getServer().getOnlinePlayers()) {
            if (other.isOp())
                continue;
            other.hidePlayer(player);
        }

        invisible.add(player);
        player.sendMessage(ChatColor.RED + "Poof!");
    }

    public void disableVanish(Player player) {
        for (Player other : getServer().getOnlinePlayers()) {
            other.showPlayer(player);
        }

        invisible.remove(player);
        player.sendMessage(ChatColor.RED + "You have reappeared!");
    }
}