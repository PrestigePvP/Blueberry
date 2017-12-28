package me.missionary.blueberry.scoreboard.impl;

import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.profile.Profile;
import me.missionary.blueberry.scoreboard.Scoreboard;
import me.missionary.blueberry.scoreboard.interfaces.BoardProvider;
import me.missionary.blueberry.scoreboard.timer.DateTimeFormats;
import me.missionary.blueberry.scoreboard.timer.Format;
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
       Blueberry.getPlugin().getProfileManager().getProfile(player.getUniqueId()).ifPresent(profile -> {
           for (String line : SCOREBOARD_LINES) {
               if (line.contains("%kills%")) {
                   entries.add(line.replace("%kills%", String.valueOf(profile.getKills())));
               } else if (line.contains("%deaths%")) {
                   entries.add(line.replace("%deaths%", String.valueOf(profile.getDeaths())));
               } else if (line.contains("%date%")) {
                   entries.add(line.replace("%date%", DateTimeFormats.getFormattedTimeBasedOnTimeZone(profile.getTimeZone())));
               }
           }
           Scoreboard.getPlayer(player).getTimers().forEach(timer -> entries.add(timer.getName() + ": " + timer.getFormattedString()));
       });
       return entries;
    }

    @Override
    public String getTitle(Player player) {
        return SCOREBOARD_TITLE;
    }
}
