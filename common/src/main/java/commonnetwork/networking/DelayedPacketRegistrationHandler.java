package commonnetwork.networking;

import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DelayedPacketRegistrationHandler implements PacketRegistrar
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

    /**
     * Register packet.
     * Defaults handling the packet on the main thread.
     *
     * @param packetIdentifier - the packet identifier
     * @param messageType      - the messageType
     * @param encoder          - the encoder
     * @param decoder          - the decoder
     * @param handler          - the handler
     * @param <T>
     * @return
     */
    @Override
    public <T> PacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        return registerPacket(packetIdentifier, messageType, encoder, decoder, handler, false);
    }

    /**
     * Register packet.
     *
     * @param packetIdentifier      - the packet identifier
     * @param messageType           - the messageType
     * @param encoder               - the encoder
     * @param decoder               - the decoder
     * @param handler               - the handler
     * @param handleOnNetworkThread - to handle on the network thread
     * @param <T>
     * @return
     */
    @Override
    public <T> PacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler, boolean handleOnNetworkThread)
    {
        PacketContainer<T> container = new PacketContainer<>(packetIdentifier, messageType, encoder, decoder, handler, handleOnNetworkThread);
        QUEUED_PACKET_MAP.put(messageType, container);
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
