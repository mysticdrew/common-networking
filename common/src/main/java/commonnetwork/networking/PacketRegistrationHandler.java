package commonnetwork.networking;


import commonnetwork.api.NetworkHandler;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class PacketRegistrationHandler implements NetworkHandler, PacketRegistrar
{
    final Map<Class<?>, PacketContainer<?, ? extends ByteBuf>> PACKET_MAP = new HashMap<>();

    protected final Side side;

    /**
     * Handles packet registration
     *
     * @param side - The side
     */
    public PacketRegistrationHandler(Side side)
    {
        this.side = side;
    }

    public <T, B extends FriendlyByteBuf> PacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> packetClass, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T, B> container = new PacketContainer<>(packetIdentifier, packetClass, encoder, decoder, handler);
        PACKET_MAP.put(packetClass, container);
        registerPacket(container);
        return this;
    }

    @Override
    public <T, B extends FriendlyByteBuf> PacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<B, T> codec, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T, B> container = new PacketContainer<>(type, packetClass, codec, handler);
        PACKET_MAP.put(packetClass, container);
        registerPacket(container);
        return this;
    }

    public Side getSide()
    {
        return side;
    }

    abstract <T, B extends FriendlyByteBuf> void registerPacket(PacketContainer<T, B> container);

}
