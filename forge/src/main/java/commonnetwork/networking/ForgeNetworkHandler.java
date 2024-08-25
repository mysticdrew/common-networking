package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ForgeNetworkHandler extends PacketRegistrationHandler
{
    private final Map<Class<?>, Message<?>> CHANNELS = new HashMap<>();

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
                        T message = container.decoder().apply(event.getPayload());
                        buildHandler(container.handler()).accept(message, event.getSource());
                    });
            CHANNELS.put(container.classType(), new Message<>(channel, container.encoder()));
        }
    }

    public <T> void sendToServer(T packet)
    {
        this.sendToServer(packet, false);
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {

        var message = (Message<T>) CHANNELS.get(packet.getClass());
        if (message != null)
        {
            EventNetworkChannel channel = message.channel();
            Connection connection = Minecraft.getInstance().getConnection().getConnection();

            if (ignoreCheck || channel.isRemotePresent(connection))
            {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                message.encoder().accept(packet, buf);
                channel.send(buf, connection);
            }
        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the client, packets need to be registered on both sides!");
        }

    }

    public <T> void sendToClient(T packet, ServerPlayer player)
    {

        var message = (Message<T>) CHANNELS.get(packet.getClass());
        EventNetworkChannel channel = message.channel();
        Connection connection = player.connection.getConnection();
        if (message != null)
        {
            if (channel.isRemotePresent(connection))
            {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                message.encoder().accept(packet, buf);
                channel.send(buf, connection);
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

    public record Message<T>(EventNetworkChannel channel, BiConsumer<T, FriendlyByteBuf> encoder)
    {
    }
}
