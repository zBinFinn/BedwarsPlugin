package org.zbinfinn.minigameplugin.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.zbinfinn.minigameplugin.MinigamePlugin;
import org.zbinfinn.minigameplugin.instance.Arena;
import org.zbinfinn.minigameplugin.manager.ConfigManager;

public class ConnectListener implements Listener {

    private MinigamePlugin minigame;

    public ConnectListener(MinigamePlugin minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.teleport(ConfigManager.getLobbySpawn());
        player.setGameMode(GameMode.CREATIVE);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Arena arena = minigame.getArenaManager().getArena(e.getPlayer());
        if (arena != null) {
            arena.removePlayer(e.getPlayer());
        }
    }

}
