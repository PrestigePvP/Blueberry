package me.missionary.blueberry.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Created by Missionary (missionarymc@gmail.com) on 3/14/2017.
 */
@UtilityClass
public class LocationUtils {

    public Location generateLocFromString(String value) {
        if (value == null){
            return null;
        }

        String[] values = value.split(";");

        World world = Bukkit.getWorld(values[0]);
        Double x = Double.parseDouble(values[1]);
        Double y = Double.parseDouble(values[2]);
        Double z = Double.parseDouble(values[3]);

        return new Location(world, x, y, z);
    }

    public String getLocationAsString(Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();
    }

    public Vector deserialize(String input){
        if (input == null){
            return null;
        }
        String[] split = input.split(";");
        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);
        return new Vector(x, y , z);
    }

    public String serializeVector(Vector input){
        return input.getX() + ";" + input.getY() + ";" + input.getZ();
    }
}
