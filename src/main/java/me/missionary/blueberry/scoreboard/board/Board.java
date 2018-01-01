/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can/and will result in further action against the unauthorized user(s).
 */

package me.missionary.blueberry.scoreboard.board;

import lombok.Getter;
import me.missionary.blueberry.scoreboard.timer.Timer;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Missionary on on 6/4/2017.
 */
public class Board implements Listener {

    private final WeakReference<Player> player;
    @Getter
    private final Scoreboard scoreboard;
    @Getter
    private final Objective objective;
    private BukkitTask task;
    private AtomicBoolean isDisplayingScoreboard = new AtomicBoolean(true);
    private Set<me.missionary.blueberry.scoreboard.timer.Timer> timers = net.minecraft.util.com.google.common.collect.Sets.newConcurrentHashSet();

    public Board(me.missionary.blueberry.scoreboard.Scoreboard lilac, Player player) {
        this.player = new WeakReference<>(player);

        if (!Bukkit.getServer().getScoreboardManager().getMainScoreboard().equals(player.getScoreboard())) {
            scoreboard = player.getScoreboard();
        } else {
            scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
            player.setScoreboard(scoreboard);
        }

        Objective checkObjective;
        if ((checkObjective = scoreboard.getObjective("Scoreboard")) != null) {
            checkObjective.unregister();
        }

        objective = scoreboard.registerNewObjective("Scoreboard", "dummy");
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', lilac.getProvider().getTitle(player)));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Arrays.stream(ChatColor.values()).forEach(colour -> {
            Team team = scoreboard.getTeam(colour.toString()) == null ? scoreboard.registerNewTeam(colour.toString()) : scoreboard.getTeam(colour.toString());
            if (!team.getEntries().contains(colour.toString())) {
                team.addEntry(colour.toString());
            }
        });

        task = new BukkitRunnable() {

            @Override
            public void run() {
                List<String> provided = lilac.getProvider().provide(getPlayer(), getTimers());
                scoreboard.getEntries().forEach(scoreboard::resetScores);
                if (!provided.isEmpty() && isDisplayingScoreboard.get()) {
                    String title = lilac.getProvider().getTitle(player);
                    objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));

                    int score;
                    if (lilac.getOptions().countUp()) {
                        score = 1;
                        Collections.reverse(provided);
                    } else {
                        score = 15;
                    }

                    for (String ln : provided) {
                        Team team = scoreboard.getTeam(ChatColor.values()[score].toString());
                        String text = ChatColor.translateAlternateColorCodes('&', ln);
                        if (text.length() > 16) {
                            String first = text.substring(0, 16);
                            String second = text.substring(16, text.length());
                            if (first.endsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
                                first = first.substring(0, first.length() - 1);
                                second = ChatColor.COLOR_CHAR + second;
                            }
                            String lastColors = ChatColor.getLastColors(first);
                            second = lastColors + second;
                            team.setPrefix(first);
                            team.setSuffix(StringUtils.left(second, 16));
                        } else {
                            team.setSuffix("");
                            team.setPrefix(text);
                        }
                        objective.getScore(team.getName()).setScore(score);

                        if (lilac.getOptions().countUp()) {
                            if (score >= 15) break;
                            score++;
                        } else {
                            if (score <= 1) break;
                            score--;
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(lilac.getPlugin(), 2L, 2L);
    }

    public Player getPlayer() {
        if (player.isEnqueued()) {
            throw new RuntimeException("Player is offline");
        }
        return player.get();
    }

    public void cancelTask() {
        task.cancel();
    }

    public me.missionary.blueberry.scoreboard.timer.Timer getTimer(String id) {
        return getTimers().stream().filter(timer -> timer.getName().equals(id)).findFirst().orElse(null);
    }

    public Set<Timer> getTimers() {
        timers.removeIf(timer -> System.currentTimeMillis() >= timer.getEnd());
        return timers;
    }
}
