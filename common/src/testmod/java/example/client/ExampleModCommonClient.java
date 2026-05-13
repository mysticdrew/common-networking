package example.client;

import commonnetwork.api.Dispatcher;
import commonnetwork.api.Network;
import example.network.ExamplePacketOne;

public class ExampleModCommonClient
{
    public ExampleModCommonClient()
    {
    }

    /**
     * Fire a packet to the server when the player finishes joining a world.
     * Demonstrates both call styles — Dispatcher (static) and the handler ref.
     */
    public void onJoinWorld()
    {
        Dispatcher.sendToServer(new ExamplePacketOne());
        // Equivalent to the static call above.
        Network.getNetworkHandler().sendToServer(new ExamplePacketOne());
    }
}
