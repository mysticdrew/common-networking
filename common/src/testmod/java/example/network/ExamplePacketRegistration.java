package example.network;

import commonnetwork.api.Network;

public class ExamplePacketRegistration
{
    public void init()
    {
        Network
                .registerPacket(ExamplePacketOne.TYPE, ExamplePacketOne.STREAM_CODEC, ExamplePacketOne::handle)
                .registerPacket(ExamplePacketTwo.TYPE, ExamplePacketTwo.STREAM_CODEC, ExamplePacketTwo::handle);
    }
}
