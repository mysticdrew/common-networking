package example.paper.network;

import commonnetwork.networking.data.PacketContext;
import example.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class PaperExamplePacketOne implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<PaperExamplePacketOne> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "example_packet_one"));
    public static final StreamCodec<FriendlyByteBuf, PaperExamplePacketOne> STREAM_CODEC = StreamCodec.ofMember(PaperExamplePacketOne::encode, PaperExamplePacketOne::new);

    public PaperExamplePacketOne()
    {
    }

    public PaperExamplePacketOne(FriendlyByteBuf buf)
    {
    }

    public void encode(FriendlyByteBuf buf)
    {
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public static void handle(PacketContext<PaperExamplePacketOne> ctx)
    {
        ctx.sender().sendSystemMessage(Component.literal("ExamplePacketOne received on the server"));
    }
}
