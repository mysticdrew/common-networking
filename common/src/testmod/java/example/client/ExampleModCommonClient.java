package example.client;

import commonnetwork.api.Dispatcher;
import commonnetwork.api.Network;
import example.network.ExamplePacketOne;
import example.network.ExamplePacketRegistration;

public class ExampleModCommonClient
{
    public ExampleModCommonClient()
    {
        new ExamplePacketRegistration().init();
    }

    /**
     * Fire a packet to the server when the player finishes joining a world.
     * Demonstrates both call styles - Dispatcher (static) and the handler ref.
     */
    public void onJoinWorld()
    {
        Dispatcher.sendToServer(new ExamplePacketOne());
        // Equivalent to the static call above.
        Network.getNetworkHandler().sendToServer(new ExamplePacketOne());
    }
}
