/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can/and will result in further action against the unauthorized user(s).
 */

package me.missionary.blueberry.scoreboard.board;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.missionary.blueberry.scoreboard.timer.Timer;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by Missionary on on 6/4/2017.
 */
public class Board implements Listener {

    private final WeakReference<Player> player;
    private final Scoreboard scoreboard;
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
                if (provided != null && !provided.isEmpty() && isDisplayingScoreboard.get()) {
                    String title = lilac.getProvider().getTitle(player);
                    objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
                    List<String> lines = net.minecraft.util.com.google.common.collect.Lists.newCopyOnWriteArrayList();
                    lines.addAll(provided.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));


                    int score;
                    if (lilac.getOptions().countUp()) {
                        score = 1;
                        Collections.reverse(lines);
                    } else {
                        score = 15;
                    }

                    for (String line : lines) {
                        Team team = scoreboard.getTeam(ChatColor.values()[score].toString());
                        if (line.length() < 16) {
                            team.setPrefix(line);
                            team.setSuffix("");
                            lines.add(line);
                        } else {
                            int splitAt = line.toCharArray()[14] == ChatColor.COLOR_CHAR ? 14 : 15;
                            team.setPrefix(line.substring(0, splitAt));
                            String remainder = (ChatColor.getLastColors(team.getPrefix()) + line.substring(splitAt));
                            team.setSuffix(remainder.length() > 16 ? remainder.substring(0, Math.min(remainder.length(), 16)) : remainder);
                            lines.add(team.getPrefix() + team.getSuffix());
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


                    Set<String> finalLines = net.minecraft.util.com.google.common.collect.Sets.newConcurrentHashSet(lines);
                    scoreboard.getEntries().forEach(entry -> {
                        Team team = scoreboard.getTeam(entry);
                        if (team != null && !finalLines.contains(team.getPrefix() + (team.getSuffix() == null ? "" : team.getSuffix()))) {
                            scoreboard.resetScores(entry);
                        }
                    });
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
