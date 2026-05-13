package commonnetwork.networking;

import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DelayedPacketRegistrationHandler implements PacketRegistrar
{
    private static final Map<CustomPacketPayload.Type<?>, PacketContainer<?>> QUEUED_PACKET_MAP = new HashMap<>();

    @Override
    public Side getSide()
    {
        return Side.CLIENT;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> PacketRegistrar registerPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        QUEUED_PACKET_MAP.put(type, new PacketContainer<>(type, (StreamCodec<? super FriendlyByteBuf, T>) codec, handler, PacketContainer.PacketType.PLAY));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> PacketRegistrar registerConfigurationPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        QUEUED_PACKET_MAP.put(type, new PacketContainer<>(type, (StreamCodec<? super FriendlyByteBuf, T>) codec, handler, PacketContainer.PacketType.CONFIGURATION));
        return this;
    }

    public void registerQueuedPackets(PacketRegistrationHandler packetRegistration)
    {
        if (!QUEUED_PACKET_MAP.isEmpty())
        {
            packetRegistration.PACKET_MAP.putAll(QUEUED_PACKET_MAP);
            QUEUED_PACKET_MAP.values().forEach(packetRegistration::onRegister);
            QUEUED_PACKET_MAP.clear();
        }
    }
}
