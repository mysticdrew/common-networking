package example.network;

import commonnetwork.api.Network;

public class TestRegistration
{

    public void init()
    {
        Network
                .registerPacket(ExamplePacketOne.CHANNEL, ExamplePacketOne.class, ExamplePacketOne::encode, ExamplePacketOne::decode, ExamplePacketOne::handle)
                .registerPacket(ExamplePacketTwo.CHANNEL, ExamplePacketTwo.class, ExamplePacketTwo::encode, ExamplePacketTwo::decode, ExamplePacketTwo::handle);
    }
}
