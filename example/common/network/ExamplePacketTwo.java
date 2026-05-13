package example.network;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class ExamplePacketTwo implements CustomPacketPayload
{
    public static final Identifier CHANNEL = new Identifier(Constants.MOD_ID, "example_packet_two");
    public static final CustomPacketPayload.Type<ExamplePacketTwo> TYPE = new CustomPacketPayload.Type<>(CHANNEL);
    public static final StreamCodec<RegistryFriendlyByteBuf, ExamplePacketTwo> STREAM_CODEC = StreamCodec.ofMember(ExamplePacketTwo::encode, ExamplePacketTwo::new);

    public ExamplePacketTwo()
    {
    }

    public ExamplePacketTwo(RegistryFriendlyByteBuf buf)
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

    public static void handle(PacketContext<ExamplePacketTwo> ctx)
    {
        if (Side.CLIENT.equals(ctx.side()))
        {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("ExamplePacketTwo on the client!"));
        }
        else
        {
            ctx.sender().sendSystemMessage(Component.literal("ExamplePacketTwo received on the server"));
        }
    }
}
