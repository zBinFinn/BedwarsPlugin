package org.zbinfinn.minigameplugin.instance;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.zbinfinn.minigameplugin.MinigamePlugin;
import org.zbinfinn.minigameplugin.manager.ConfigManager;

import java.util.UUID;

public class Countdown extends BukkitRunnable {

    private MinigamePlugin minigame;
    private Arena arena;
    private int countdownSeconds;

    public Countdown(MinigamePlugin minigame, Arena arena) {
        this.minigame = minigame;
        this.arena = arena;
        this.countdownSeconds = ConfigManager.getCountdownSeconds();
    }

    public void start() {
        arena.setState(GameState.COUNTDOWN);
        runTaskTimer(minigame, 0, 20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            arena.start();
            return;
        }

        if (countdownSeconds <= 10 || countdownSeconds % 15 == 0) {
            arena.sendMessage("Game will start in " + countdownSeconds + "second" + ((countdownSeconds > 1) ? "s" : "") + ".");
        }

        arena.sendTitle(countdownSeconds + " Second" + ((countdownSeconds > 1) ? "s" : ""), " until Game starts");

        countdownSeconds--;
    }

}
