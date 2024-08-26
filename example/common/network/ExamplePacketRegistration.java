package example.network;

import commonnetwork.api.Network;

public class TestRegistration
{

    public void init()
    {
        Network
                .registerPacket(ExamplePacketOne.type(), ExamplePacketOne.class, ExamplePacketOne.STREAM_CODEC, ExamplePacketOne::handle)
                .registerPacket(ExamplePacketTwo.type(), ExamplePacketTwo.class, ExamplePacketTwo.STREAM_CODEC, ExamplePacketTwo::handle);
    }
}
