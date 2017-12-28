/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can/and will result in further action against the unauthorized user(s).
 */
package me.missionary.blueberry.scoreboard.interfaces;

import me.missionary.blueberry.scoreboard.timer.Timer;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Set;

/**
 * Created by Missionary on on 6/4/2017.
 */
public interface BoardProvider {

    /**
     * Provides the list for the board.
     *
     * @param player The player for the board.
     * @return The list with the entries.
     */
    List<String> provide(Player player, Set<Timer> timers);

    /**
     * Get the title of the board.
     *
     * @param player The player for the board.
     * @return The board title.
     */
    String getTitle(Player player);
}
