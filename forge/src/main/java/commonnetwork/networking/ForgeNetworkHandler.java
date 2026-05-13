package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkProtocol;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ForgeNetworkHandler extends PacketRegistrationHandler
{
    private final Map<CustomPacketPayload.Type<?>, Channel<CustomPacketPayload>> CHANNELS = new HashMap<>();

    public ForgeNetworkHandler(Side side)
    {
        super(side);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <T extends CustomPacketPayload> void onRegister(PacketContainer<T> container)
    {
        if (CHANNELS.containsKey(container.type()))
        {
            return;
        }
        var channelBuilder = ChannelBuilder.named(container.type().id()).optional().payloadChannel();
        BiConsumer<T, CustomPayloadEvent.Context> handler = buildHandler(container.handler());
        Channel<CustomPacketPayload> channel;
        if (container.packetType() == PacketContainer.PacketType.PLAY)
        {
            channel = channelBuilder.play().bidirectional()
                    .addMain(container.type(), (StreamCodec<RegistryFriendlyByteBuf, T>) (StreamCodec) container.codec(), handler)
                    .build();
        }
        else
        {
            channel = channelBuilder.configuration().bidirectional()
                    .addMain(container.type(), (StreamCodec<FriendlyByteBuf, T>) (StreamCodec) container.codec(), handler)
                    .build();
        }
        CHANNELS.put(container.type(), channel);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T packet, boolean ignoreCheck)
    {
        requireContainer(packet);
        Channel<CustomPacketPayload> channel = CHANNELS.get(packet.type());
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        if (ignoreCheck || channel.isRemotePresent(connection))
        {
            channel.send(packet, connection);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        requireContainer(packet);
        Channel<CustomPacketPayload> channel = CHANNELS.get(packet.type());
        Connection connection = player.connection.getConnection();
        if (ignoreCheck || channel.isRemotePresent(connection))
        {
            channel.send(packet, connection);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void send(T packet, Connection connection)
    {
        Channel<CustomPacketPayload> channel = CHANNELS.get(packet.type());
        if (channel != null)
        {
            channel.send(packet, connection);
        }
    }

    @Override
    public <T extends CustomPacketPayload> @Nullable ClientboundCustomPayloadPacket getRawClientboundPacket(T packet)
    {
        Channel<CustomPacketPayload> channel = CHANNELS.get(packet.type());
        if (channel != null)
        {
            return (ClientboundCustomPayloadPacket) ((Object) NetworkProtocol.PLAY.buildPacket(PacketFlow.CLIENTBOUND, channel, packet));
        }
        return null;
    }

    @Override
    public <T extends CustomPacketPayload> @Nullable ServerboundCustomPayloadPacket getRawServerboundPacket(T packet)
    {
        Channel<CustomPacketPayload> channel = CHANNELS.get(packet.type());
        if (channel != null)
        {
            return (ServerboundCustomPayloadPacket) ((Object) NetworkProtocol.PLAY.buildPacket(PacketFlow.SERVERBOUND, channel, packet));
        }
        return null;
    }

    private <T> BiConsumer<T, CustomPayloadEvent.Context> buildHandler(Consumer<PacketContext<T>> handler)
    {
        return (message, ctx) -> {
            try
            {
                ctx.setPacketHandled(true);
                ctx.enqueueWork(() -> {
                    Side packetSide = ctx.isServerSide() ? Side.SERVER : Side.CLIENT;
                    ServerPlayer player = ctx.getSender();
                    handler.accept(new PacketContext<>(player, message, packetSide));
                });
            }
            catch (Throwable t)
            {
                Constants.LOG.error("{} error handling packet", message.getClass(), t);
            }
        };
    }
}
