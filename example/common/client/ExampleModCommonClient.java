package example.client;

import commonnetwork.api.Dispatcher;
import commonnetwork.api.Network;
import example.network.ExamplePacketOne;

public class ExampleModCommonClient
{
    public ExampleModCommonClient()
    {
        // initialize packets on the client.
        new TestRegistration().init();
    }

    public void onJoinWorld() {
        Dispatcher.sendToServer(new ExamplePacketOne());
        // either works fine.
        Network.getDispatcher().sendToServer(new ExamplePacketOne());
    }
}
