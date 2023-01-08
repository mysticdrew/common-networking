package commonnetwork.test_packets;

import commonnetwork.api.CommonNetworking;

public class TestRegistration
{

    public void init()
    {
        CommonNetworking.registerPacket(ExamplePacketOne.CHANNEL, ExamplePacketOne.class, ExamplePacketOne::encode, ExamplePacketOne::decode, ExamplePacketOne::handle);
        CommonNetworking.registerPacket(ExamplePacketTwo.CHANNEL, ExamplePacketTwo.class, ExamplePacketTwo::encode, ExamplePacketTwo::decode, ExamplePacketTwo::handle);
    }
}
