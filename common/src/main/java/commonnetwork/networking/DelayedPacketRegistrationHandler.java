package commonnetwork.networking;

import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DelayedPacketRegistrationHandler  implements PacketRegistrar
{
    private static final Map<Class<?>, PacketContainer<?>> QUEUED_PACKET_MAP = new HashMap<>();


    public DelayedPacketRegistrationHandler()
    {

    }

    @Override
    public Side getSide()
    {
        return Side.CLIENT;
    }

    @Override
    public <T> PacketRegistrar registerPacket(ResourceLocation id, Class<T> packetClass, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(id, packetClass, encoder, decoder, handler);
        QUEUED_PACKET_MAP.put(packetClass, container);
        return this;
    }

    @Override
    public <T, B extends FriendlyByteBuf> PacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<B, T> codec, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(type, packetClass, codec, handler);
        QUEUED_PACKET_MAP.put(packetClass, container);
        return this;
    }

    public void registerQueuedPackets(PacketRegistrationHandler packetRegistration)
    {
        if (!QUEUED_PACKET_MAP.isEmpty())
        {
            packetRegistration.PACKET_MAP.putAll(QUEUED_PACKET_MAP);
            QUEUED_PACKET_MAP.forEach((aClass, container) -> packetRegistration.registerPacket(container));
        }
    }
}
