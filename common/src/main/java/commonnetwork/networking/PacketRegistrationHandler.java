package commonnetwork.networking;


import commonnetwork.api.NetworkHandler;
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

public abstract class PacketRegistrationHandler implements NetworkHandler, PacketRegistrar
{
    final Map<Class<?>, PacketContainer<?>> PACKET_MAP = new HashMap<>();

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

    public <T> PacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(packetIdentifier, messageType, encoder, decoder, handler);
        PACKET_MAP.put(messageType, container);
        registerPacket(container);
        return this;
    }

    public Side getSide()
    {
        return side;
    }

    abstract <T> void registerPacket(PacketContainer<T> container);

}
