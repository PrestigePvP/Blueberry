package me.missionary.blueberry.listeners;

import me.missionary.blueberry.scoreboard.Scoreboard;
import me.missionary.blueberry.scoreboard.board.Board;
import me.missionary.blueberry.scoreboard.timer.DateTimeFormats;
import me.missionary.blueberry.scoreboard.timer.Timer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/28/2017.
 */
public class EnderpearlListener implements Listener {

    public static final String ENDERPEARL_COOLDOWN_KEY = "Enderpearl";

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Board board = Scoreboard.getPlayer(player);
        if (event.getAction().name().contains("RIGHT")) {

            if (event.getItem() == null || event.getItem().getType() != Material.ENDER_PEARL) {
                return;
            }
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                return;
            }

            if (board.getTimer(ENDERPEARL_COOLDOWN_KEY) != null) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(ChatColor.RED + "Enderpearl Cooldown: " + ChatColor.YELLOW + DateTimeFormats.getRemaining(board.getTimer(ENDERPEARL_COOLDOWN_KEY).getEnd() - System.currentTimeMillis(), true, true));
                return;
            }

            new Timer(Scoreboard.getPlayer(player), ENDERPEARL_COOLDOWN_KEY, 15L);
        }
    }
}
