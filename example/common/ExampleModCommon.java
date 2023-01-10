package example.common;

import commonnetwork.api.Dispatcher;
import commonnetwork.api.Network;
import example.network.ExamplePacketOne;
import example.network.TestRegistration;
import net.minecraft.server.level.ServerPlayer;

public class ExampleModCommon
{
    public ExampleModCommon()
    {
        // initialize and register packets
        new ExamplePacketRegistration().init();
    }

    public void onPlayerJoinServer(ServerPlayer player) {
        Dispatcher.sendToClient(new ExamplePacketOne(), player);
        // either works fine.
        Network.getNetworkHandler().sendToClient(new ExamplePacketOne(), player);
    }
}
