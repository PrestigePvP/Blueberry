package me.missionary.blueberry.combatlogger;

import me.missionary.blueberry.Blueberry;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class LogoutTaskManager {

    private final Map<UUID, LogoutTask> taskMap = new WeakHashMap<>(); // Use a weak map because it doesn't really matter what happens to the contents at GC.

    private final Blueberry plugin;

    public LogoutTaskManager(Blueberry plugin) {
        this.plugin = plugin;
    }

    public void addLogoutTask(LogoutTask logoutTask){
        if (taskMap.containsKey(logoutTask.getPlayer().getUniqueId())){
            plugin.getLogger().warning("LogoutTaskManager - taskMap already contains LogoutTask for " + logoutTask.getPlayer().getUniqueId());
            return;
        }
        taskMap.put(logoutTask.getPlayer().getUniqueId(), logoutTask);
    }

    public boolean isLoggingOut(Player player){
        return taskMap.containsKey(player.getUniqueId());
    }

    public Optional<LogoutTask> getLogoutTask(Player player){
        return Optional.ofNullable(taskMap.get(player.getUniqueId()));
    }

    public void removeLogoutTask(LogoutTask task){
        taskMap.remove(task.getPlayer().getUniqueId());
    }
}
