package me.missionary.blueberry.combatlogger.entity;


import me.missionary.blueberry.combatlogger.LoggerRemoveReason;
import me.missionary.blueberry.combatlogger.event.LoggerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.UUID;

// CREDIT https://github.com/ijoeleoli/LoggerAPI
public class LoggerEntity {

    public static final String KILLED_METADATA = "KILLED";

    private static final int EXPIRE_TIME = 15;

    private Plugin plugin;
    private UUID uuid;
    private String name;
    private double health;
    private ItemStack[] contents;
    private ItemStack[] armour;
    private World world;
    private Villager villager;
    private BukkitTask despawnTask;

    public LoggerEntity(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.health = player.getHealth();
        this.contents = player.getInventory().getContents();
        this.armour = player.getInventory().getArmorContents();
        this.world = player.getWorld();

        this.villager = (Villager) this.world.spawnEntity(player.getLocation(), EntityType.VILLAGER);
        this.villager.setCustomName(this.name);
        this.villager.setHealth(this.health);

        this.resetTimer();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(this.uuid);
    }

    public Location getLocation() {
        return this.villager.getLocation();
    }

    public void remove(Player killer, LoggerRemoveReason reason) {
        this.despawnTask.cancel();

        if (reason == LoggerRemoveReason.REJOIN || reason == LoggerRemoveReason.EXPIRED) {
            this.villager.remove();
            return;
        }

        LoggerDeathEvent event = new LoggerDeathEvent(this, killer);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        Location location = this.villager.getLocation();

        Arrays.stream(this.contents).forEach(item -> this.world.dropItemNaturally(location, item));

        Arrays.stream(this.armour).forEach(item -> this.world.dropItemNaturally(location, item));

        getPlayer().setMetadata(KILLED_METADATA, new FixedMetadataValue(plugin, null));
    }

    public void resetTimer() {
        if (this.despawnTask != null) {
            this.despawnTask.cancel();
        }

        this.despawnTask = Bukkit.getScheduler().runTaskLater(plugin, () -> remove(null, LoggerRemoveReason.EXPIRED), EXPIRE_TIME * 20);
    }

}