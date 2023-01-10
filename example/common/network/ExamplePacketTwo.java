package example.network;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ExamplePacketTwo
{
    public static final ResourceLocation CHANNEL = new ResourceLocation(Constants.MOD_ID, "example_packet_two");

    public ExamplePacketTwo()
    {
    }

    public static ExamplePacketTwo decode(FriendlyByteBuf buf)
    {
        return new ExamplePacketTwo();
    }

    public void encode(FriendlyByteBuf buf)
    {

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
