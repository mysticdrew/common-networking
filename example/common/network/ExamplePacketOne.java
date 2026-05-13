package example.network;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class ExamplePacketOne implements CustomPacketPayload
{
    public static final Identifier CHANNEL = new Identifier(Constants.MOD_ID, "example_packet_one");
    public static final CustomPacketPayload.Type<ExamplePacketOne> TYPE = new CustomPacketPayload.Type<>(CHANNEL);
    public static final StreamCodec<FriendlyByteBuf, ExamplePacketOne> STREAM_CODEC = StreamCodec.ofMember(ExamplePacketOne::encode, ExamplePacketOne::new);

    public ExamplePacketOne()
    {
    }

    public ExamplePacketOne(FriendlyByteBuf buf)
    {
    }

    public void encode(FriendlyByteBuf buf)
    {

    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public static void handle(PacketContext<ExamplePacketOne> ctx)
    {
        if (Side.CLIENT.equals(ctx.side()))
        {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("ExamplePacketOne on the client!"));
        }
        else
        {
            ctx.sender().sendSystemMessage(Component.literal("ExamplePacketOne received on the server"));
        }
    }
}
