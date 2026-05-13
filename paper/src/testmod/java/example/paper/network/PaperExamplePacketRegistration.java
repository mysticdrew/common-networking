package example.paper.network;

import commonnetwork.api.Network;

public class PaperExamplePacketRegistration
{
    public void init()
    {
        Network
                .registerPacket(PaperExamplePacketOne.type(), PaperExamplePacketOne.class, PaperExamplePacketOne.STREAM_CODEC, PaperExamplePacketOne::handle)
                .registerPacket(PaperExamplePacketTwo.type(), PaperExamplePacketTwo.class, PaperExamplePacketTwo.STREAM_CODEC, PaperExamplePacketTwo::handle);
    }
}
