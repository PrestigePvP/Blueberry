package me.missionary.blueberry.spawn;

import lombok.RequiredArgsConstructor;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.combatlogger.entity.LoggerEntity;
import me.missionary.blueberry.kit.KitManager;
import me.missionary.blueberry.kit.inventory.KitSelectionInventory;
import me.missionary.blueberry.listeners.CombatTagListener;
import me.missionary.blueberry.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
@RequiredArgsConstructor
public class SpawnListeners implements Listener {

    private final Blueberry plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getProfileManager().getProfiles().containsKey(event.getPlayer().getUniqueId())) {
            plugin.getProfileManager().createProfile(event.getPlayer().getUniqueId());
        }
        Player player = event.getPlayer();
        if (player.hasMetadata(LoggerEntity.KILLED_METADATA)) {
            player.setHealth(0.0);
            return;
        }
        event.setJoinMessage(null);
        player.teleport(plugin.getSpawnManager().getSpawnPoint() == null ? Bukkit.getWorld("world").getSpawnLocation() : plugin.getSpawnManager().getSpawnPoint());
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.sendMessage(ChatColor.AQUA + "Welcome to FFA!");
        player.getInventory().setItem(0, KitManager.KIT_SELECTOR);
        plugin.getProfileManager().getProfile(player.getUniqueId()).ifPresent(profile1 -> plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> plugin.getProfileManager().getPlayerTimeZone(profile1)));
    }

    @EventHandler
    public void onClickItem(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getAction().name().contains("RIGHT") && event.getItem().isSimilar(KitManager.KIT_SELECTOR)) {
            new KitSelectionInventory(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        if (plugin.getSpawnManager().contains(event.getTo()) && Scoreboard.getPlayer(event.getPlayer()).getTimer(CombatTagListener.COMBAT_TAG_KEY) != null) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getSpawnManager().contains(player.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (plugin.getSpawnManager().contains(player.getLocation())) {
            event.setCancelled(true);
        }
    }
}
