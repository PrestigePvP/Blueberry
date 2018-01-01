package me.missionary.blueberry.listeners;

import lombok.RequiredArgsConstructor;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

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
            plugin.getProfileManager().getProfile(killer.getUniqueId()).ifPresent(profile -> {
                profile.setKills(profile.getKills() + 1);
                profile.setKillsThisGame(profile.getKillsThisGame() + 1);
            });
            killer.sendMessage(ChatColor.GOLD + "You killed " + killed.getName() + "!");
            killed.sendMessage(ChatColor.GOLD + killer.getName() + " killed you with " + ChatColor.RED + "\u2764" + (killer.getHealth() / 2.0) + ChatColor.GOLD + " hearts left.");
        }

        plugin.getProfileManager().getProfile(killed.getUniqueId()).ifPresent(profile -> profile.setDeaths(profile.getDeaths() + 1));


        List<ItemStack> removeThis = new ArrayList<>();
        for (ItemStack drop : event.getDrops()) {
            if (drop.getType() != Material.POTION) {
                removeThis.add(drop);
            }
        }

        event.getDrops().removeAll(removeThis);
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.teleport(plugin.getSpawnManager().getSpawnPoint() == null ? Bukkit.getWorld("world").getSpawnLocation() : plugin.getSpawnManager().getSpawnPoint());
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItem(0, KitManager.KIT_SELECTOR);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5, 2)); // Prevent any nasty damage if spawn isn't set.
    }
}
