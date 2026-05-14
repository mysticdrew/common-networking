package example.paper.network;

import commonnetwork.api.Network;

public class PaperExamplePacketRegistration
{
    public void init()
    {
        Network
                .registerPacket(PaperExamplePacketOne.TYPE, PaperExamplePacketOne.STREAM_CODEC, PaperExamplePacketOne::handle)
                .registerPacket(PaperExamplePacketTwo.TYPE, PaperExamplePacketTwo.STREAM_CODEC, PaperExamplePacketTwo::handle);
    }
}
