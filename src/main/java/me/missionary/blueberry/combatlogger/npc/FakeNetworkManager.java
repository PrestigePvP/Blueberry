package me.missionary.blueberry.combatlogger.npc;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

import javax.crypto.SecretKey;
import java.net.SocketAddress;

/**
 * Created by Missionary (missionarymc@gmail.com) on 12/27/2017.
 */
public class FakeNetworkManager extends NetworkManager {

    public FakeNetworkManager() {
        super(false);
    }

    @Override
    public int getVersion() {
        return 4; // 1.7.2 is the oldest compat version for 1.7.10
    }

    @Override
    public void channelActive(ChannelHandlerContext channelhandlercontext) {
    }

    @Override
    public void a(EnumProtocol enumprotocol) {
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelhandlercontext) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) {
    }

    @Override
    protected void a(ChannelHandlerContext channelhandlercontext, Packet packet) {
    }

    @Override
    public void a(PacketListener packetlistener) {
    }

    @Override
    public void handle(Packet packet, GenericFutureListener... agenericfuturelistener) {
    }

    @Override
    public void a() {

    }

    @Override
    public SocketAddress getSocketAddress() {
        return null; // no sockadd for false players
    }

    @Override
    public void close(IChatBaseComponent ichatbasecomponent) {
    }

    @Override
    public boolean c() {
        return false; // throw a false val instead of super method
    }

    @Override
    public void a(SecretKey secretkey) {
    }

    @Override
    public boolean isConnected() {
        return false; // sp00ky
    }

    @Override
    public PacketListener getPacketListener() {
        return null;
    }

    @Override
    public IChatBaseComponent f() {
        return null;
    }

    @Override
    public void g() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelhandlercontext, Object object) {
    }

    @Override
    public SocketAddress getRawAddress() {
        return null; // Fake players dont need no address
    }

    @Override
    public void enableCompression() {
    }
}
