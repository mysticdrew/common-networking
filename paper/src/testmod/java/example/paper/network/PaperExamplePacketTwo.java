package example.paper.network;

import commonnetwork.networking.data.PacketContext;
import example.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class PaperExamplePacketTwo implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<PaperExamplePacketTwo> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "example_packet_two"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PaperExamplePacketTwo> STREAM_CODEC = StreamCodec.ofMember(PaperExamplePacketTwo::encode, PaperExamplePacketTwo::new);

    public PaperExamplePacketTwo()
    {
    }

    public PaperExamplePacketTwo(RegistryFriendlyByteBuf buf)
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

    public static void handle(PacketContext<PaperExamplePacketTwo> ctx)
    {
        ctx.sender().sendSystemMessage(Component.literal("ExamplePacketTwo received on the server"));
    }
}
