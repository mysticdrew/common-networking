package example.network;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ExamplePacketOne
{
    public static final ResourceLocation CHANNEL = new ResourceLocation(Constants.MOD_ID, "example_packet_one");
    public static final StreamCodec<FriendlyByteBuf, ExamplePacketOne> STREAM_CODEC = StreamCodec.ofMember(ExamplePacketOne::encode, ExamplePacketTwo::new);

    public ExamplePacketOne()
    {
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type()
    {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public ExamplePacketOne(FriendlyByteBuf buf)
    {
    }

    public void encode(FriendlyByteBuf buf)
    {

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
