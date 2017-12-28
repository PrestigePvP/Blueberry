package me.missionary.blueberry.combatlogger;

import lombok.Getter;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.combatlogger.commands.LogoutCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class LogoutTask {

    private final Blueberry plugin;
    @Getter
    private final Player player;
    @Getter
    private final BukkitTask bukkitTask;
    private final Location playerLocation;
    @Getter
    private int time = 30;
    private LogoutTask logoutTask;

    public LogoutTask(Blueberry plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.playerLocation = player.getLocation();
        this.bukkitTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new LogoutTaskRunnable(), 0, 20);
        plugin.getLogoutTaskManager().addLogoutTask(this);
        this.logoutTask = this;
    }

    private class LogoutTaskRunnable implements Runnable {

        @Override
        public void run() {
            if (player.isDead()) {
                bukkitTask.cancel();
                return;
            }

            if (time == 0) {
                player.kickPlayer(LogoutCommand.LOGOUT_MESSAGE);
                bukkitTask.cancel();
                return;
            }

            Location checkLoc = player.getLocation();

            if (checkLoc.distanceSquared(playerLocation) > 1.5) { // Grant them some grace
                player.sendMessage("The logout has been cancelled because of movement!");
                bukkitTask.cancel();
                plugin.getLogoutTaskManager().removeLogoutTask(logoutTask);
                return;
            }
            player.sendMessage(ChatColor.GRAY + "Logging out, " + ChatColor.AQUA + time + "s" + ChatColor.GRAY + " left.");
            time--; // Decrement the time.
        }
    }
}
