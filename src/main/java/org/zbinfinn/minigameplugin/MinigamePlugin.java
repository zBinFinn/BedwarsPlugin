package org.zbinfinn.minigameplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.zbinfinn.minigameplugin.command.ArenaCommand;
import org.zbinfinn.minigameplugin.listener.ConnectListener;
import org.zbinfinn.minigameplugin.listener.GameListener;
import org.zbinfinn.minigameplugin.manager.ArenaManager;
import org.zbinfinn.minigameplugin.manager.ConfigManager;

public final class MinigamePlugin extends JavaPlugin {

    private ArenaManager arenaManager;

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);

        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);

        getCommand("arena").setExecutor(new ArenaCommand(this));

        arenaManager = new ArenaManager(this);
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

}

