package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.CommonPacketWrapper;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ForgeNetworkHandler extends PacketRegistrationHandler
{
    //    private final Map<Class<?>, EventNetworkChannel> CHANNELS = new HashMap<>();
    private final Map<Class<?>, Message<?>> CHANNELS = new HashMap<>();

    public ForgeNetworkHandler(Side side)
    {
        super(side);
    }

    protected <T> void registerPacket(PacketContainer<T> container)
    {
        if (CHANNELS.get(container.classType()) == null)
        {
            var channelBuilder = ChannelBuilder.named(container.type().id()).optional().payloadChannel();
            Channel<CustomPacketPayload> channel;
            if (container.packetType() == PacketContainer.PacketType.PLAY)
            {
                channel = channelBuilder
                        .play()
                        .bidirectional()
                        .addMain(container.getType(), container.getCodec(), (msg, ctx) -> buildHandler(container.handler())
                                .accept((T) msg.packet(), ctx))
                        .build();
            }
            else
            {
                channel = channelBuilder
                        .configuration()
                        .bidirectional()
                        .addMain(container.getType(), container.getCodec(), (msg, ctx) -> buildHandler(container.handler())
                                .accept((T) msg.packet(), ctx)
                        ).build();
            }

//            var channel = ChannelBuilder.named(container.type().id()).optional().eventNetworkChannel()
//                    .addListener(event -> {
//                        CommonPacketWrapper<T> msg = container.getCodec().decode(event.getPayload());
//                        buildHandler(container.handler()).accept(msg.packet(), event.getSource());
//                    });
            CHANNELS.put(container.classType(), new Message<>(channel, container));
        }
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {

        var message = (Message<T>) CHANNELS.get(packet.getClass());
        if (message != null)
        {
            var channel = message.channel();
            Connection connection = Minecraft.getInstance().getConnection().getConnection();
            if (ignoreCheck || channel.isRemotePresent(connection))
            {
//                FriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), Minecraft.getInstance().player.registryAccess());
//                message.container.codec().encode(buf, packet);
                channel.send(new CommonPacketWrapper(message.container, packet), connection);
            }
        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the client, packets need to be registered on both sides!");
        }

    }

    public <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {

        var message = (Message<T>) CHANNELS.get(packet.getClass());
        if (message != null)
        {
            var channel = message.channel();
            Connection connection = player.connection.getConnection();
            if (ignoreCheck || channel.isRemotePresent(connection))
            {
//                FriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.server.registryAccess());
//                message.container.codec().encode(buf, packet);
                channel.send(new CommonPacketWrapper(message.container, packet), connection);
            }

        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the server, packets need to be registered on both sides!");
        }
    }

    @Override
    public <T> void send(T packet, Connection connection)
    {
        var message = (Message<T>) CHANNELS.get(packet.getClass());
        if (message != null)
        {
            var channel = message.channel();
            channel.send(new CommonPacketWrapper(message.container, packet), connection);
        }
    }

    private static void handle(CustomPacketPayload customPacketPayload, CustomPayloadEvent.Context ctx)
    {
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

    public record Message<T>(Channel<CustomPacketPayload> channel, PacketContainer<T> container)
    {
    }
}
