package commonnetwork.api;

import commonnetwork.CommonNetwork;
import commonnetwork.networking.data.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;

public class Dispatch implements NetworkHandler
{
    @Override
    public <T> void sendToServer(T packet)
    {
        CommonNetwork.getInstance().getNetworkHandler().sendToServer(packet);
    }

    @Override
    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        CommonNetwork.getInstance().getNetworkHandler().sendToServer(packet, ignoreCheck);
    }

    @Override
    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        CommonNetwork.getInstance().getNetworkHandler().sendToClient(packet, player);
    }

    @Override
    public <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        CommonNetwork.getInstance().getNetworkHandler().sendToClient(packet, player, ignoreCheck);
    }
}
