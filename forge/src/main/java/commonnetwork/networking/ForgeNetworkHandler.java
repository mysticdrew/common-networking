package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.CommonPacketWrapper;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ForgeNetworkHandler extends PacketRegistrationHandler
{
    private final Map<Class<?>, EventNetworkChannel> CHANNELS = new HashMap<>();

    public ForgeNetworkHandler(Side side)
    {
        super(side);
    }

    protected <T> void registerPacket(PacketContainer<T> container)
    {
        if (CHANNELS.get(container.classType()) == null)
        {
            var channel = ChannelBuilder.named(container.type().id()).optional().eventNetworkChannel()
                    .addListener(event -> {
                        CommonPacketWrapper<T> msg = container.getCodec().decode(event.getPayload());
                        buildHandler(container.handler()).accept(msg.packet(), event.getSource());
                    });
            CHANNELS.put(container.classType(), channel);
        }
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {

        Channel<T> channel = (Channel<T>) CHANNELS.get(packet.getClass());
        if (channel != null)
        {
            Connection connection = Minecraft.getInstance().getConnection().getConnection();
            if (ignoreCheck || channel.isRemotePresent(connection))
            {
                channel.send(packet, connection);
            }
        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the client, packets need to be registered on both sides!");
        }

    }

    public <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {

        Channel<T> channel = (Channel<T>) CHANNELS.get(packet.getClass());

        Connection connection = player.connection.getConnection();
        if (channel != null)
        {
            if (ignoreCheck || channel.isRemotePresent(connection))
            {
                channel.send(packet, connection);
            }

        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the server, packets need to be registered on both sides!");
        }
    }


    private <T> BiConsumer<T, CustomPayloadEvent.Context> buildHandler(Consumer<PacketContext<T>> handler)
    {
        return (message, ctx) -> {
            try
            {
                ctx.setPacketHandled(true);
                ctx.enqueueWork(() -> {
                    Side side = ctx.isServerSide() ? Side.SERVER : Side.CLIENT;
                    ServerPlayer player = ctx.getSender();
                    handler.accept(new PacketContext<>(player, message, side));
                });
            }
            catch (Throwable t)
            {
                Constants.LOG.error("{} error handling packet", message.getClass(), t);
            }
        };
    }

//    public record Message<T>(EventNetworkChannel channel, BiConsumer<T, ? extends FriendlyByteBuf> encoder)
//    {
//    }
}
