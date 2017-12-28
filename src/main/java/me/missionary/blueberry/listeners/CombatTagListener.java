package me.missionary.blueberry.listeners;

import me.missionary.blueberry.scoreboard.Scoreboard;
import me.missionary.blueberry.scoreboard.timer.Timer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class CombatTagListener implements Listener {

    public static final String COMBAT_TAG_KEY = "Combat Tag";

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player || (event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player)) {
                System.out.println("CALLED COMBATTAGLISTENER - ONEDE"); // TODO: 12/27/2017 GIVE THE TAGGER A TAG
                Player player = (Player) event.getEntity();
                if (Scoreboard.getPlayer(player).getTimer(COMBAT_TAG_KEY) == null) {
                    new Timer(Scoreboard.getPlayer(player), COMBAT_TAG_KEY, 30L);
                } else {
                    Scoreboard.getPlayer(player).getTimer(COMBAT_TAG_KEY).setNewEnd(30L);
                }
            }
        }
    }
}
