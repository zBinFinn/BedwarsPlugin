package org.zbinfinn.minigameplugin.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.zbinfinn.minigameplugin.instance.Arena;
import org.zbinfinn.minigameplugin.MinigamePlugin;
import org.zbinfinn.minigameplugin.instance.BedLocation;
import org.zbinfinn.minigameplugin.instance.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaManager {

    private List<Arena> arenas = new ArrayList<Arena>();

    public ArenaManager(MinigamePlugin minigame) {
        FileConfiguration config = minigame.getConfig();
        for (String str : config.getConfigurationSection("arenas.").getKeys(false)) {
            World world = Bukkit.createWorld(new WorldCreator(config.getString("arenas." + str + ".world")));
            HashMap<Team, Location> spawns = new HashMap<Team, Location>();
            for (String team : config.getConfigurationSection("arenas." + str + ".spawns.").getKeys(false)) {
                spawns.put(Team.valueOf(team.toUpperCase()), new Location(
                        world,
                        config.getDouble("arenas." + str + ".spawns." + team + ".x"),
                        config.getDouble("arenas." + str + ".spawns." + team + ".y"),
                        config.getDouble("arenas." + str + ".spawns." + team + ".z"),
                        (float) config.getDouble("arenas." + str + ".spawns." + team + ".yaw"),
                        (float) config.getDouble("arenas." + str + ".spawns." + team + ".pitch")
                ));
            }
            HashMap<Team, BedLocation> beds = new HashMap<Team, BedLocation>();
            for (String team : config.getConfigurationSection("arenas." + str + ".beds.").getKeys(false)) {
                beds.put(Team.valueOf(team.toUpperCase()), new BedLocation(
                        world,
                        config.getDouble("arenas." + str + ".beds." + team + ".x"),
                        config.getDouble("arenas." + str + ".beds." + team + ".y"),
                        config.getDouble("arenas." + str + ".beds." + team + ".z"),
                        BlockFace.valueOf(config.getString("arenas." + str + ".beds." + team + ".facing").toUpperCase())
                ));
            }



            arenas.add(new Arena(minigame, Integer.parseInt(str), Integer.parseInt(config.getString("arenas." + str + ".y-respawn")), spawns, beds));
        }
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public Arena getArena(Player player) {
        for (Arena arena : arenas) {
            if (arena.getPlayers().contains(player.getUniqueId())) {
                return arena;
            }
        }
        return null;
    }

    public Arena getArena(int id) {
        for (Arena arena : arenas) {
            if (arena.getId() == id) {
                return arena;
            }
        }
        return null;
    }

}
