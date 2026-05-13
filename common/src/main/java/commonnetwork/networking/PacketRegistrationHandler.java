package commonnetwork.networking;


import commonnetwork.api.NetworkHandler;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class PacketRegistrationHandler implements NetworkHandler, PacketRegistrar
{
    final Map<CustomPacketPayload.Type<?>, PacketContainer<?>> PACKET_MAP = new HashMap<>();

    protected final Side side;

    public PacketRegistrationHandler(Side side)
    {
        this.side = side;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> PacketRegistrar registerPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(type, (StreamCodec<? super FriendlyByteBuf, T>) codec, handler, PacketContainer.PacketType.PLAY);
        PACKET_MAP.put(type, container);
        onRegister(container);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> PacketRegistrar registerConfigurationPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(type, (StreamCodec<? super FriendlyByteBuf, T>) codec, handler, PacketContainer.PacketType.CONFIGURATION);
        PACKET_MAP.put(type, container);
        onRegister(container);
        return this;
    }

    @Override
    public Side getSide()
    {
        return side;
    }

    @Override
    public <T extends CustomPacketPayload> @Nullable ClientboundCustomPayloadPacket getRawClientboundPacket(T packet)
    {
        if (PACKET_MAP.containsKey(packet.type()))
        {
            return new ClientboundCustomPayloadPacket(packet);
        }
        return null;
    }

    @Override
    public <T extends CustomPacketPayload> @Nullable ServerboundCustomPayloadPacket getRawServerboundPacket(T packet)
    {
        if (PACKET_MAP.containsKey(packet.type()))
        {
            return new ServerboundCustomPayloadPacket(packet);
        }
        return null;
    }

    /**
     * Looks up the registered {@link PacketContainer} for the given packet, throwing if it is not registered.
     */
    @SuppressWarnings("unchecked")
    protected <T extends CustomPacketPayload> PacketContainer<T> requireContainer(T packet)
    {
        PacketContainer<T> container = (PacketContainer<T>) PACKET_MAP.get(packet.type());
        if (container == null)
        {
            throw new RegistrationException(packet.getClass() + " packet not registered on the " + side + ", packets need to be registered on both sides!");
        }
        return container;
    }

    /**
     * Loader-specific wiring for a freshly-registered packet. May be a no-op if the loader defers registration to its own lifecycle event.
     */
    protected abstract <T extends CustomPacketPayload> void onRegister(PacketContainer<T> container);

}
