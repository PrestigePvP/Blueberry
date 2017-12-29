package me.missionary.blueberry.combatlogger;

import lombok.RequiredArgsConstructor;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.combatlogger.entity.LoggerEntity;
import me.missionary.blueberry.scoreboard.Scoreboard;
import me.missionary.blueberry.utils.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

import static me.missionary.blueberry.combatlogger.commands.LogoutCommand.LOGOUT_MESSAGE;
import static me.missionary.blueberry.listeners.CombatTagListener.COMBAT_TAG_KEY;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
@RequiredArgsConstructor
public class LogoutListener implements Listener {

    private static final String BYPASS_PERMISSION = "blueberry.nologger";

    private final Blueberry plugin;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(BYPASS_PERMISSION)) {
            return;
        }

        if (player.getHealth() <= 0D) {
            return;
        }

        boolean isInCombat = Scoreboard.getPlayer(player).getTimer(COMBAT_TAG_KEY) != null;

        Optional<LogoutTask> logoutTask = plugin.getLogoutTaskManager().getLogoutTask(player);

        logoutTask.ifPresent(task -> {
            if (task.getTime() <= 0) { // done
                return;
            }
            task.getBukkitTask().cancel();
            plugin.getLogoutTaskManager().removeLogoutTask(task);
        });

        if (!isInCombat || plugin.getSpawnManager().contains(player.getLocation())) {
            player.teleport(plugin.getSpawnManager().getSpawnPoint()); // Teleport the player to spawn.
            player.kickPlayer(LOGOUT_MESSAGE);
            return;
        }

        PlayerUtil.recursivelyRemoveFromVehicle(player);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                return;
            }
            plugin.getLoggerManager().addLogger(new LoggerEntity(plugin, player));
        }, 1L);

    }
}
