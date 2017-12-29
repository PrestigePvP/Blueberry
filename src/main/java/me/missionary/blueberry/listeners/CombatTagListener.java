package me.missionary.blueberry.listeners;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.scoreboard.Scoreboard;
import me.missionary.blueberry.scoreboard.timer.Timer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class CombatTagListener implements Listener {

    public static final String COMBAT_TAG_KEY = "Combat Tag";

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (Blueberry.getPlugin().getSpawnManager().contains(event.getEntity().getLocation())) {
                return;
            }
            if (event.getDamager() instanceof Player || (event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player)) {
                System.out.println("CALLED COMBATTAGLISTENER - ONEDE");
                Player player = (Player) event.getEntity();
                if (Scoreboard.getPlayer(player).getTimer(COMBAT_TAG_KEY) == null) {
                    new Timer(Scoreboard.getPlayer(player), COMBAT_TAG_KEY, 30L);
                } else {
                    Scoreboard.getPlayer(player).getTimer(COMBAT_TAG_KEY).setNewEnd(30L);
                }
                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    if (Scoreboard.getPlayer(damager).getTimer(COMBAT_TAG_KEY) == null) {
                        new Timer(Scoreboard.getPlayer(damager), COMBAT_TAG_KEY, 30L);
                    } else {
                        Scoreboard.getPlayer(damager).getTimer(COMBAT_TAG_KEY).setNewEnd(30L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBowHitEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Player shooter = (Player) arrow.getShooter();
                    player.sendMessage(ChatColor.GOLD + "You are now at " + ChatColor.RED + player.getHealth() / 2 + ChatColor.GOLD + " hearts.");
                    shooter.sendMessage(player.getName() + ChatColor.GOLD +  " is now at " + ChatColor.RED + player.getHealth() / 2 + ChatColor.GOLD + " hearts.");
                }
            }
        }
    }
}
