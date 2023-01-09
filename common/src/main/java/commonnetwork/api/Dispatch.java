package commonnetwork.api;

import commonnetwork.networking.PacketRegistrationHandler;
import commonnetwork.networking.data.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;

public class Dispatch implements NetworkHandler
{
    private static Dispatch INSTANCE;
    private final PacketRegistrationHandler handler;

    public Dispatch(PacketRegistrationHandler handler)
    {
        INSTANCE = this;
        this.handler = handler;
    }

    private static Dispatch getInstance()
    {
        if (INSTANCE != null)
        {
            return INSTANCE;
        }
        throw new ExceptionInInitializerError("Dispatch is not initialized!");
    }

    @Override
    public <T> void sendToServer(T packet)
    {
        getInstance().handler.sendToServer(packet);
    }

    @Override
    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        getInstance().handler.sendToServer(packet, ignoreCheck);
    }

    @Override
    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        getInstance().handler.sendToClient(packet, player);
    }

    @Override
    public <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        getInstance().handler.sendToClient(packet, player, ignoreCheck);
    }
}
