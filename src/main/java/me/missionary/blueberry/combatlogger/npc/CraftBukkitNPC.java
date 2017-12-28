package me.missionary.blueberry.combatlogger.npc;

import me.missionary.blueberry.Blueberry;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class CraftBukkitNPC extends CraftPlayer {

    private final Player player;
    private RemovalRunnable runnable;
    private final int despawnAfter;

    public CraftBukkitNPC(CraftServer server, EntityNPC entity, Player player, int despawnAfter) {
        super(server, entity);
        this.player = player;
        this.despawnAfter = despawnAfter;
        this.runnable = new RemovalRunnable();
        runnable.runTaskLater(Blueberry.getPlugin(), despawnAfter);
    }

    @Override
    public EntityNPC getHandle() {
        return (EntityNPC) super.getHandle();
    }

    public void despawnNPC() {
        runnable.cancel();
        saveData();
        remove();

        Entity vehicle = getVehicle();

        while (vehicle != null) {
            vehicle.remove();
            vehicle = vehicle.getVehicle();
        }
    }

    public void resetDespawnTimer() {
        runnable.cancel();
        runnable = new RemovalRunnable();
        runnable.runTaskLater(Blueberry.getPlugin(), despawnAfter);
    }

    private final class RemovalRunnable extends BukkitRunnable {

        @Override
        public void run() {
            despawnNPC();
        }
    }
}
