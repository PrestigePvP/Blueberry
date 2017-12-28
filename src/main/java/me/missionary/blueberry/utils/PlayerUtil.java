package me.missionary.blueberry.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
@UtilityClass
public class PlayerUtil {

    public void recursivelyRemoveFromVehicle(Player player) {
        Entity veh = player.getVehicle();

        while (veh != null) {
            veh.eject();
            player.teleport(veh.getLocation());
            recursivelyRemoveFromVehicle(player);
        }
    }
}
