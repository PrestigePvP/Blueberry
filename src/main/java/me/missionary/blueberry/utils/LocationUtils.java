package me.missionary.blueberry.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Missionary (missionarymc@gmail.com) on 3/14/2017.
 */
@UtilityClass
public class LocationUtils {

    public Location generateLocFromString(String value) {
        String[] values = value.split(",");

        World world = Bukkit.getWorld(values[0]);
        Double x = Double.parseDouble(values[1]);
        Double y = Double.parseDouble(values[2]);
        Double z = Double.parseDouble(values[3]);

        return new Location(world, x, y, z);
    }

    public String getLocationAsString(Location location) {
        return location.toString();
    }
}
