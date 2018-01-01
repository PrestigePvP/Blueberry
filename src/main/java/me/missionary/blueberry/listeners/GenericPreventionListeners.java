package me.missionary.blueberry.listeners;

import lombok.RequiredArgsConstructor;
import me.missionary.blueberry.Blueberry;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/30/17.
 */
@RequiredArgsConstructor
public class GenericPreventionListeners implements Listener {

    private final Blueberry plugin;

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("blueberry.admin")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            event.setCancelled(true);
        }
    }
}
