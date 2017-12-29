package me.missionary.blueberry.combatlogger;

import me.missionary.blueberry.combatlogger.entity.LoggerEntity;
import me.missionary.blueberry.combatlogger.event.LoggerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// CREDIT: https://github.com/ijoeleoli/LoggerAPI
public class LoggerManager implements Listener {

    private Plugin plugin;
    private Map<String, LoggerEntity> loggerEntities = new HashMap<>();

    public LoggerManager(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public Collection<LoggerEntity> getLoggers() {
        return this.loggerEntities.values();
    }

    public LoggerEntity getLogger(String name) {
        return this.loggerEntities.get(name);
    }

    public void addLogger(LoggerEntity logger) {
        this.loggerEntities.put(logger.getName(), logger);
    }

    public void removeLogger(String name) {
        this.loggerEntities.remove(name);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!this.loggerEntities.containsKey(player.getName())) {
            return;
        }

        LoggerEntity logger = this.getLogger(player.getName());
        logger.remove(null, LoggerRemoveReason.REJOIN);

        this.loggerEntities.remove(player.getName());
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.VILLAGER) {
            Villager villager = (Villager) event.getEntity();

            if (villager.getCustomName() == null) {
                return;
            }

            if (!this.loggerEntities.containsKey(villager.getCustomName())) {
                return;
            }

            LoggerEntity logger = this.getLogger(villager.getCustomName());

            Player player = null;

            if (event.getDamager().getType() == EntityType.PLAYER) {
                player = (Player) event.getDamager();
            }

            if (villager.getHealth() - event.getDamage() <= 0.0) {
                LoggerDeathEvent e = new LoggerDeathEvent(logger, player);
                Bukkit.getServer().getPluginManager().callEvent(e);

                if (!e.isCancelled()) {
                    logger.remove(player, LoggerRemoveReason.KILLED);
                }
            } else {
                logger.resetTimer();
            }
        }
    }

}