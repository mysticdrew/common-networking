package commonnetwork.networking;

import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DelayedPacketRegistrationHandler  implements PacketRegistrar
{
    private static final Map<Class<?>, PacketContainer<?>> QUEUED_PACKET_MAP = new HashMap<>();
    private static final Map<Identifier, PacketContainer<?>> QUEUED_PACKET_BY_ID = new HashMap<>();


    public DelayedPacketRegistrationHandler()
    {

    }

    @Override
    public Side getSide()
    {
        return Side.CLIENT;
    }

    @Override
    public <T> PacketRegistrar registerPacket(Identifier id, Class<T> packetClass, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(id, packetClass, encoder, decoder, handler);
        QUEUED_PACKET_MAP.put(packetClass, container);
        QUEUED_PACKET_BY_ID.put(container.type().id(), container);
        return this;
    }

    @Override
    public <T> PacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(type, packetClass, codec, handler, PacketContainer.PacketType.PLAY);
        QUEUED_PACKET_MAP.put(packetClass, container);
        QUEUED_PACKET_BY_ID.put(container.type().id(), container);
        return this;
    }

    @Override
    public <T> PacketRegistrar registerConfigurationPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(type, packetClass, codec, handler, PacketContainer.PacketType.CONFIGURATION);
        QUEUED_PACKET_MAP.put(packetClass, container);
        QUEUED_PACKET_BY_ID.put(container.type().id(), container);
        return this;
    }

    public void registerQueuedPackets(PacketRegistrationHandler packetRegistration)
    {
        if (!QUEUED_PACKET_MAP.isEmpty())
        {
            QUEUED_PACKET_MAP.forEach((aClass, container) ->
            {
                if (!packetRegistration.supports(container.packetType()))
                {
                    throw new RegistrationException("Backend does not support packet type: " + container.packetType());
                }
                packetRegistration.PACKET_MAP.put(aClass, container);
                packetRegistration.packetById.put(container.type().id(), container);
                packetRegistration.registerPacket(container);
            });
        }
    }
}
