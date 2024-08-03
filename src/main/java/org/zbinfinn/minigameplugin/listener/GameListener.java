package org.zbinfinn.minigameplugin.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.zbinfinn.minigameplugin.MinigamePlugin;
import org.zbinfinn.minigameplugin.instance.Arena;
import org.zbinfinn.minigameplugin.instance.Game;
import org.zbinfinn.minigameplugin.instance.GameState;
import org.zbinfinn.minigameplugin.instance.Team;

public class GameListener implements Listener {

    private MinigamePlugin minigame;

    public GameListener(MinigamePlugin minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {
        Arena arena = minigame.getArenaManager().getArena(e.getPlayer());

        if (arena != null && arena.getState().equals(GameState.LIVE)) {
            Game game = arena.getGame();
            if (e.getBlock().getType() == Material.RED_BED && e.getBlock().hasMetadata("team")) {
                e.setCancelled(game.destroyBed(Team.valueOf(e.getBlock().getMetadata("team").get(0).asString()), e.getPlayer()));
            }
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e) {
        e.deathMessage(Component.text(""));
        Arena arena = minigame.getArenaManager().getArena(e.getPlayer());
        if (arena != null && arena.getState().equals(GameState.LIVE)) {
            Game game = arena.getGame();
            game.death(e.getPlayer());
        }

    }

    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        Arena arena = minigame.getArenaManager().getArena(e.getPlayer());
        if (arena != null && arena.getState().equals(GameState.LIVE)) {
            e.setRespawnLocation(arena.getGame().respawn(e.getPlayer()));
        }
    }
}
