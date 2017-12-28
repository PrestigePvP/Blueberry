package me.missionary.blueberry.combatlogger.npc;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Location;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class FakePlayerConnection extends PlayerConnection { // holy fuck that is a lot of methods named 'a'

    public FakePlayerConnection(EntityPlayer entityplayer) {
        super(MinecraftServer.getServer(), new FakeNetworkManager(), entityplayer);
    }

    @Override
    public boolean isDisconnected() {
        return false;
    }

    @Override
    public void a() {
    }

    @Override
    public void disconnect(String s) {
    }

    @Override
    public void a(PacketPlayInSteerVehicle packetplayinsteervehicle) {
    }

    @Override
    public void a(PacketPlayInFlying packetplayinflying) {
    }

    @Override
    public void a(double d0, double d1, double d2, float f, float f1) {
    }

    @Override
    public void teleport(Location dest) {
    }

    @Override
    public void a(PacketPlayInBlockDig packetplayinblockdig) {
    }

    @Override
    public void a(PacketPlayInBlockPlace packetplayinblockplace) {
    }

    @Override
    public void a(IChatBaseComponent ichatbasecomponent) {
    }

    @Override
    public void sendPacket(Packet packet) {
    }

    @Override
    public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot) {
    }

    @Override
    public void a(PacketPlayInChat packetplayinchat) {
    }

    @Override
    public void chat(String s, boolean async) {
    }

    @Override
    public void a(PacketPlayInArmAnimation packetplayinarmanimation) {
    }

    @Override
    public void a(PacketPlayInEntityAction packetplayinentityaction) {
    }

    @Override
    public void a(PacketPlayInUseEntity packetplayinuseentity) {
    }

    @Override
    public void a(PacketPlayInClientCommand packetplayinclientcommand) {
    }

    @Override
    public void a(PacketPlayInCloseWindow packetplayinclosewindow) {
    }

    @Override
    public void a(PacketPlayInWindowClick packetplayinwindowclick) {
    }

    @Override
    public void a(PacketPlayInEnchantItem packetplayinenchantitem) {
    }

    @Override
    public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot) {
    }

    @Override
    public void a(PacketPlayInTransaction packetplayintransaction) {
    }

    @Override
    public void a(PacketPlayInUpdateSign packetplayinupdatesign) {
    }

    @Override
    public void a(PacketPlayInKeepAlive packetplayinkeepalive) {
    }

    @Override
    public void a(PacketPlayInAbilities packetplayinabilities) {
    }

    @Override
    public void a(PacketPlayInTabComplete packetplayintabcomplete) {
    }

    @Override
    public void a(PacketPlayInSettings packetplayinsettings) {
    }

    @Override
    public void a(PacketPlayInCustomPayload packetplayincustompayload) {
    }

    @Override
    public void a(EnumProtocol enumprotocol, EnumProtocol enumprotocol1) {
    }
}
