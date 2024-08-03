package org.zbinfinn.minigameplugin.instance;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.zbinfinn.minigameplugin.MinigamePlugin;
import org.zbinfinn.minigameplugin.manager.ConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Arena {

    private MinigamePlugin minigame;

    private int yRespawn;
    private int id;
    private HashMap<Team, Location> spawns;
    private HashMap<Team, BedLocation> beds;

    private GameState state;
    private List<UUID> players;
    private Countdown countdown;
    private Game game;

    public Arena(MinigamePlugin minigame, int id, int yRespawn, HashMap<Team, Location> spawns, HashMap<Team, BedLocation> beds) {
        this.yRespawn = yRespawn;
        this.id = id;
        this.spawns = spawns;

        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
        this.minigame = minigame;
        this.countdown = new Countdown(minigame, this);
        this.game = new Game(this);
        this.beds = beds;
    }
    /* GAME */

    public void start() {
        game.start();
    }

    public void reset(boolean kickPlayers) {
        if (kickPlayers) {
            Location location = ConfigManager.getLobbySpawn();
            for (UUID uuid : players) {
                Bukkit.getPlayer(uuid).teleport(location);
            }
            players.clear();
        }
        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(minigame, this);
        game.cancelTasks();
        game = new Game(this);
    }


    /* TOOLS */

    public void sendMessage(String message) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title, String subtitle) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle, 20, 0, 2);
        }
    }

    public void sendTitle(String title, String subtitle, int duration, int fadeIn, int fadeOut) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle, duration, fadeIn, fadeOut);
        }
    }

    /* PLAYERS */

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());

        if (state.equals(GameState.RECRUITING) && players.size() >= ConfigManager.getRequiredPlayers()) {
            countdown.start();
        }

    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        player.teleport(ConfigManager.getLobbySpawn());

        int requiredPlayers = ConfigManager.getRequiredPlayers();
        if (state == GameState.COUNTDOWN && players.size() < requiredPlayers) {
            sendMessage("There aren't enough players. Countdown stopped.");
            reset(false);
        }

        if (state == GameState.LIVE && players.size() < requiredPlayers) {
            sendMessage("The game ended as too many people left.");
            reset(false);
        }
    }


    /* GET & SET */

    public int getId() { return id; }
    public List<UUID> getPlayers() { return players; }
    public Game getGame() { return game; }
    public HashMap<Team, Location> getSpawns() { return spawns; }
    public HashMap<Team, BedLocation> getBeds() { return beds; }
    public int getyRespawn() { return yRespawn; }
    public MinigamePlugin getMinigame() { return minigame; }

    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }
}
