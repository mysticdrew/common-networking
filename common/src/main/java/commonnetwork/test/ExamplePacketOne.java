package commonnetwork.test;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ExamplePacketOne
{
    public static final ResourceLocation CHANNEL = new ResourceLocation(Constants.MOD_ID, "example_packet_one");

    private String message;

    public ExamplePacketOne(String message)
    {
        this.message = message;
    }

    public static ExamplePacketOne decode(FriendlyByteBuf buf)
    {
        return new ExamplePacketOne(buf.readUtf(32767));
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeUtf(this.message);
    }

    public static void handle(PacketContext<ExamplePacketOne> ctx)
    {
        System.out.println("Packet Received on " + ctx.side() + " With Message: " + ctx.message().message);
    }
}
