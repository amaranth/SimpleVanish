package com.probablycoding.bukkit.simplevanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SimpleVanishListener implements Listener {
    private SimpleVanish plugin;

    SimpleVanishListener(SimpleVanish instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp())
            return;

        for (Player hidden : plugin.invisible) {
            event.getPlayer().hidePlayer(hidden);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.invisible.contains(event.getPlayer()))
            return;

        for (Player other : plugin.getServer().getOnlinePlayers()) {
            other.showPlayer(event.getPlayer());
        }
    }
}