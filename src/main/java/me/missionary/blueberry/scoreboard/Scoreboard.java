/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can/and will result in further action against the unauthorized user(s).
 */

package me.missionary.blueberry.scoreboard;

import lombok.Getter;
import lombok.Setter;
import me.missionary.blueberry.scoreboard.board.Board;
import me.missionary.blueberry.scoreboard.interfaces.BoardProvider;
import net.minecraft.util.com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Missionary on on 6/4/2017.
 */
public class Scoreboard implements Listener {

    private static final Map<UUID, Board> boards = Maps.newConcurrentMap();

    @Getter
    private JavaPlugin plugin;
    @Getter
    private Options options;
    @Getter
    @Setter
    private BoardProvider provider;

    /**
     * Creates a new Scoreboard instance, therefore creating a new SB for each player online.
     *
     * @param plugin   The plugin that's using this API {@link Scoreboard}.
     * @param options  The options that we want this sb to have. (Globally)
     * @param provider The provider that implements the methods in the interface {@link BoardProvider}.
     */
    public Scoreboard(JavaPlugin plugin, Options options, BoardProvider provider) {
        Objects.requireNonNull(plugin);
        this.plugin = plugin;
        this.options = options;
        this.provider = provider;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getOnlinePlayers().forEach(o -> {
            if (!boards.containsKey(o.getUniqueId())) {
                boards.put(o.getUniqueId(), new Board(this, o));
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        boards.put(event.getPlayer().getUniqueId(), new Board(this, event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Board board = boards.remove(event.getPlayer().getUniqueId());
        if (board != null) {
            board.cancelTask();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(plugin)) {
            boards.clear();
        }
    }

    public static Board getPlayer(Player player) {
        return boards.values().stream().filter(board -> board.getPlayer().equals(player)).findFirst().orElse(null);
    }
}
