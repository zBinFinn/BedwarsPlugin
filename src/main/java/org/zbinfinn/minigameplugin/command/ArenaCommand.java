package org.zbinfinn.minigameplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zbinfinn.minigameplugin.MinigamePlugin;
import org.zbinfinn.minigameplugin.instance.Arena;
import org.zbinfinn.minigameplugin.instance.GameState;

public class ArenaCommand implements CommandExecutor {

    private MinigamePlugin minigame;

    public ArenaCommand(MinigamePlugin minigame) {
        this.minigame = minigame;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            player.sendMessage("Available Arenas: ");
            for (Arena arena : minigame.getArenaManager().getArenas()) {
                player.sendMessage("Arena " + arena.getId() + ": " + arena.getState().name());
            }
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
            Arena arena = minigame.getArenaManager().getArena(player);
            if (arena == null) {
                player.sendMessage("You aren't in an arena");
                return false;
            }

            arena.removePlayer(player);
            player.sendMessage("You left the arena");

            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
            if (minigame.getArenaManager().getArena(player) != null) {
                player.sendMessage("You already are in an arena");
                return false;
            }
            int id;
            try {
                id = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("You didn't specify a valid arena id");
                return false;
            }

            if (id < 0 || id > (minigame.getArenaManager().getArenas().size() - 1)) {
                player.sendMessage("That arena does not exist");
                return false;
            }

            Arena arena = minigame.getArenaManager().getArena(id);

            if (arena.getPlayers().size() >= 4) {
                player.sendMessage("The game is full :(");
                return false;
            }

            if (arena.getState() == GameState.RECRUITING || arena.getState() == GameState.COUNTDOWN) {
                arena.addPlayer(player);
                player.sendMessage("You are now playing in arena " + id);
            } else {
                player.sendMessage("You can't join the arena cause a game is already running");
            }

            return false;
        }

        player.sendMessage("Invalid Usage:");
        player.sendMessage("- /arena list");
        player.sendMessage("- /arena leave");
        player.sendMessage("- /arena join <id>");

        return false;
    }

}
