package example.client;

import commonnetwork.api.Dispatcher;
import commonnetwork.api.Network;
import example.network.ExamplePacketOne;

public class ExampleModCommonClient
{
    public ExampleModCommonClient()
    {
        // initialize and register packets on the client.
        new ExamplePacketRegistration().init();
    }

    public void onJoinWorld() {
        Dispatcher.sendToServer(new ExamplePacketOne());
        // either works fine.
        Network.getNetworkHandler().sendToServer(new ExamplePacketOne());
    }
}
