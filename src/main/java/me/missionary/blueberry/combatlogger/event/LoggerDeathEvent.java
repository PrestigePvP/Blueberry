package me.missionary.blueberry.combatlogger.event;

import me.missionary.blueberry.combatlogger.entity.LoggerEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

// CREDIT: https://github.com/ijoeleoli/LoggerAPI
public class LoggerDeathEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private LoggerEntity logger;
    private Player killer;
    private boolean cancelled = false;

    public LoggerDeathEvent(LoggerEntity logger, Player killer) {
        this.logger = logger;
        this.killer = killer;
    }

    public LoggerEntity getLogger() {
        return this.logger;
    }

    public Player getKiller() {
        return this.killer;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
