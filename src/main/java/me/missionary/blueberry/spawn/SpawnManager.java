package me.missionary.blueberry.spawn;

import lombok.Getter;
import lombok.Setter;
import me.missionary.blueberry.Blueberry;
import me.missionary.blueberry.utils.LocationUtils;
import me.missionary.blueberry.utils.Manager;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/26/2017.
 */
@Getter
@Setter
public class SpawnManager extends Manager {

    private String worldName;

    private Vector min, max;

    private Location spawnPoint;

    public SpawnManager(Blueberry plugin) {
        super(plugin);
    }

    public boolean contains(Location location) {
        return location.getWorld().getName().equals(worldName) &&
                location.getX() >= min.getX() &&
                location.getX() <= max.getX() + 1 &&
                location.getZ() >= min.getZ() &&
                location.getZ() <= max.getZ() + 1 &&
                location.getY() >= min.getY() &&
                location.getY() <= max.getY() + 1;
    }

    @Override
    public void onEnable() {
        spawnPoint = LocationUtils.generateLocFromString(getPlugin().getConfig().getString("SPAWN.SPAWNPOINT"));
        min = LocationUtils.deserialize(getPlugin().getConfig().getString("SPAWN.MIN"));
        max = LocationUtils.deserialize(getPlugin().getConfig().getString("SPAWN.MAX"));
    }

    @Override
    public void onDisable() {
        getPlugin().getConfig().set("SPAWN.SPAWNPOINT", LocationUtils.getLocationAsString(spawnPoint));
        getPlugin().getConfig().set("SPAWN.MIN", LocationUtils.serializeVector(min));
        getPlugin().getConfig().set("SPAWN.MAX", LocationUtils.serializeVector(max));
    }
}
