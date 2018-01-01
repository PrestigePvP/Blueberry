package me.missionary.blueberry.scoreboard.impl;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.listeners.CombatTagListener;
import me.missionary.blueberry.listeners.EnderpearlListener;
import me.missionary.blueberry.scoreboard.Scoreboard;
import me.missionary.blueberry.scoreboard.board.Board;
import me.missionary.blueberry.scoreboard.board.BoardProvider;
import me.missionary.blueberry.scoreboard.timer.DateTimeFormats;
import me.missionary.blueberry.scoreboard.timer.Timer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/26/2017.
 */
public class BlueberryImpl implements BoardProvider {

    private static final String SCOREBOARD_TITLE = ChatColor.translateAlternateColorCodes('&', Blueberry.getPlugin().getConfig().getString("SCOREBOARD.TITLE"));

    private static final List<String> SCOREBOARD_LINES = Blueberry.getPlugin().getConfig().getStringList("SCOREBOARD.LINES");

    @Override
    public List<String> provide(Player player, Set<Timer> timers) {
        List<String> entries = new ArrayList<>();
        Board board = Scoreboard.getPlayer(player);
        Blueberry.getPlugin().getProfileManager().getProfile(player.getUniqueId()).ifPresent(profile -> {
            for (String line : SCOREBOARD_LINES) {
                if (line.contains("%kills%")) {
                    entries.add(line.replace("%kills%", String.valueOf(profile.getKills())));
                } else if (line.contains("%kills_this%")) {
                    entries.add(line.replace("%kills_this%", String.valueOf(profile.getKillsThisGame())));
                } else if (line.contains("%deaths%")) {
                    entries.add(line.replace("%deaths%", String.valueOf(profile.getDeaths())));
                } else if (line.contains("%date%")) {
                    entries.add(line.replace("%date%", DateTimeFormats.getFormattedTimeBasedOnTimeZone(profile.getTimeZone())));
                } else if (line.contains("%combattag%")) {
                    if (board.getTimer(CombatTagListener.COMBAT_TAG_KEY) != null) {
                        entries.add(line.replace("%combattag%", DateTimeFormats.getRemaining(board.getTimer(CombatTagListener.COMBAT_TAG_KEY).getEnd() - System.currentTimeMillis(), true, true)));
                    }
                } else if (line.contains("%enderpearl%")) {
                    if (board.getTimer(EnderpearlListener.ENDERPEARL_COOLDOWN_KEY) != null) {
                        entries.add(line.replace("%enderpearl%", DateTimeFormats.getRemaining(board.getTimer(EnderpearlListener.ENDERPEARL_COOLDOWN_KEY).getEnd() - System.currentTimeMillis(), true, true)));
                    }
                } else if (line.contains("%kit%")) {
                    Blueberry.getPlugin().getKitManager().getEquippedKit(player).ifPresent(kit -> entries.add(line.replace("%kit%", kit.getName())));
                } else {
                    entries.add(line);
                }
            }
        });

        return entries.size() <= 2 ? null : entries;
    }

    @Override
    public String getTitle(Player player) {
        return SCOREBOARD_TITLE;
    }
}
