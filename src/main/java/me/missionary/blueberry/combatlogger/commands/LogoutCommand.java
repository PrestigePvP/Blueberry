package me.missionary.blueberry.combatlogger.commands;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.combatlogger.LogoutTask;
import me.missionary.blueberry.listeners.CombatTagListener;
import me.missionary.blueberry.scoreboard.Scoreboard;
import me.missionary.blueberry.scoreboard.board.Board;
import me.missionary.blueberry.utils.commands.Command;
import me.missionary.blueberry.utils.commands.CommandArgs;
import me.missionary.blueberry.utils.commands.GenericArgument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class LogoutCommand extends GenericArgument {

    public static final String LOGOUT_MESSAGE = ChatColor.GREEN + "You have safely logged out!";

    public LogoutCommand(Blueberry plugin) {
        super(plugin);
    }

    @Command(name = "logout", description = "Command used to leave the server safely without loss of stats", inGameOnly = true)
    public void onCommand(CommandArgs args){
        Player player = args.getPlayer();

        if (getPlugin().getLogoutTaskManager().isLoggingOut(player)){
            player.sendMessage(ChatColor.RED + "You are already logging out!");
            return;
        }

        Board board = Scoreboard.getPlayer(player);

        if (board.getTimer(CombatTagListener.COMBAT_TAG_KEY) != null){
            getPlugin().getLogoutTaskManager().addLogoutTask(new LogoutTask(getPlugin(), player));
        } else {
            player.teleport(getPlugin().getSpawnManager().getSpawnPoint()); // Teleport the player to spawn.
            player.kickPlayer(LOGOUT_MESSAGE);
        }
    }
}
