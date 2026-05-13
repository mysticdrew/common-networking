package example;

import commonnetwork.api.Dispatcher;
import example.network.ExamplePacketRegistration;
import example.network.ExamplePacketTwo;
import net.minecraft.server.level.ServerPlayer;

public class ExampleModCommon
{
    public ExampleModCommon()
    {
        // Register both packets. Safe to call from any loader's entry point.
        new ExamplePacketRegistration().init();
    }

    /**
     * Push a packet to a player as soon as they join the server.
     */
    public void onPlayerJoinServer(ServerPlayer player)
    {
        Dispatcher.sendToClient(new ExamplePacketTwo(), player);
    }
}
