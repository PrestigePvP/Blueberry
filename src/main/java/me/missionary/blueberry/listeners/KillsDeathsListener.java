package me.missionary.blueberry.listeners;

import lombok.RequiredArgsConstructor;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/29/17.
 */
@RequiredArgsConstructor
public class KillsDeathsListener implements Listener {

    private final Blueberry plugin;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killed = event.getEntity();

        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            plugin.getProfileManager().getProfile(killer.getUniqueId()).ifPresent(profile -> profile.setKills(profile.getKills() + 1));
        }

        plugin.getProfileManager().getProfile(killed.getUniqueId()).ifPresent(profile -> profile.setDeaths(profile.getDeaths() + 1));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!killed.isOnline()) {
                    return;
                }
                killed.teleport(plugin.getSpawnManager().getSpawnPoint() == null ? Bukkit.getWorld("world").getSpawnLocation() : plugin.getSpawnManager().getSpawnPoint());
                killed.getInventory().clear();
                killed.getInventory().setArmorContents(new ItemStack[4]);
                killed.getInventory().setItem(0, KitManager.KIT_SELECTOR);
            }
        }.runTaskLater(plugin, 1L);
    }
}
