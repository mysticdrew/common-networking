package commonnetwork.networking;


import commonnetwork.api.NetworkHandler;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class PacketRegistrationHandler implements NetworkHandler, PacketRegistrar
{
    final Map<Class<?>, PacketContainer<?>> PACKET_MAP = new HashMap<>();
    final Map<Identifier, PacketContainer<?>> packetById = new HashMap<>();

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

    @Override
    public <T> PacketRegistrar registerPacket(Identifier packetIdentifier, Class<T> packetClass, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(packetIdentifier, packetClass, encoder, decoder, handler);
        if (!supports(container.packetType()))
        {
            throw new RegistrationException("Backend does not support packet type: " + container.packetType());
        }
        PACKET_MAP.put(packetClass, container);
        packetById.put(container.type().id(), container);
        registerPacket(container);
        return this;
    }

    @Override
    public <T> PacketRegistrar registerConfigurationPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(type, packetClass, codec, handler, PacketContainer.PacketType.CONFIGURATION);
        if (!supports(container.packetType()))
        {
            throw new RegistrationException("Backend does not support packet type: " + container.packetType());
        }
        PACKET_MAP.put(packetClass, container);
        packetById.put(container.type().id(), container);
        registerPacket(container);
        return this;
    }

    @Override
    public <T> PacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(type, packetClass, codec, handler, PacketContainer.PacketType.PLAY);
        if (!supports(container.packetType()))
        {
            throw new RegistrationException("Backend does not support packet type: " + container.packetType());
        }
        PACKET_MAP.put(packetClass, container);
        packetById.put(container.type().id(), container);
        registerPacket(container);
        return this;
    }

    public Side getSide()
    {
        return side;
    }

    protected boolean supports(PacketContainer.PacketType type)
    {
        return true;
    }

    protected @Nullable PacketContainer<?> getPacketContainer(Identifier id)
    {
        return packetById.get(id);
    }

    protected @Nullable PacketContainer<?> getPacketContainer(Class<?> packetClass)
    {
        return PACKET_MAP.get(packetClass);
    }

    abstract <T> void registerPacket(PacketContainer<T> container);

}
