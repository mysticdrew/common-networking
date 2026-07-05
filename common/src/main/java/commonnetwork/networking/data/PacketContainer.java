package commonnetwork.networking.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class PacketContainer<T>
{
    private final ResourceLocation packetIdentifier;
    private final Class<T> messageType;
    private final BiConsumer<T, FriendlyByteBuf> encoder;
    private final Function<FriendlyByteBuf, T> decoder;
    private final Consumer<PacketContext<T>> handler;

    public PacketContainer(ResourceLocation packetIdentifier,
                           Class<T> messageType,
                           BiConsumer<T, FriendlyByteBuf> encoder,
                           Function<FriendlyByteBuf, T> decoder,
                           Consumer<PacketContext<T>> handler)
    {
        this.packetIdentifier = packetIdentifier;
        this.messageType = messageType;
        this.encoder = encoder;
        this.decoder = decoder;
        this.handler = handler;
    }

    public ResourceLocation packetIdentifier()
    {
        return packetIdentifier;
    }

    public Class<T> messageType()
    {
        return messageType;
    }

    public BiConsumer<T, FriendlyByteBuf> encoder()
    {
        return encoder;
    }

    public Function<FriendlyByteBuf, T> decoder()
    {
        return decoder;
    }

    public Consumer<PacketContext<T>> handler()
    {
        return handler;
    }
}
