package me.missionary.blueberry.combatlogger.npc;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class EntityNPC extends EntityPlayer {

    public EntityNPC(WorldServer worldserver, Player player, int despawnAfter) {
        super(MinecraftServer.getServer(), worldserver, ((CraftPlayer) player).getProfile(), new PlayerInteractManager(worldserver)); // Call the super constructor to make a fake player
        this.invulnerableTicks = 0;
        this.playerConnection = new FakePlayerConnection(this);
        this.bukkitEntity = new CraftBukkitNPC(worldserver.getServer(), this, player, despawnAfter);
        ((CraftBukkitNPC) bukkitEntity).loadData(); // this is horrible for many reasons, mineman is gay.
        this.world.addEntity(this); // yay!
    }

    @Override
    public CraftBukkitNPC getBukkitEntity() {
        return (CraftBukkitNPC) bukkitEntity;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (super.damageEntity(damagesource, f)) {
            getBukkitEntity().resetDespawnTimer();
            return true;
        }
        return false;
    }

    @Override
    public void h() {
        super.h();
        super.C();
    }

    @Override
    public void collide(Entity entity) {
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        getBukkitEntity().despawnNPC();
    }
}
