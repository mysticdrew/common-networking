package commonnetwork.networking.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public record PacketContainer<T>(CustomPacketPayload.Type<? extends CustomPacketPayload> type,
                                 Class<T> classType,
                                 BiConsumer<T, FriendlyByteBuf> encoder,
                                 Function<FriendlyByteBuf, T> decoder,
                                 Consumer<PacketContext<T>> handler)
{
    public PacketContainer(ResourceLocation id,
                           Class<T> classType,
                           BiConsumer<T, FriendlyByteBuf> encoder,
                           Function<FriendlyByteBuf, T> decoder,
                           Consumer<PacketContext<T>> handle)
    {
        this(new CustomPacketPayload.Type<>(id), classType, encoder, decoder, handle);
    }

    @SuppressWarnings("unchecked")
    public <K extends CustomPacketPayload> CustomPacketPayload.Type<K> getType()
    {
        return (CustomPacketPayload.Type<K>) type();
    }

    public StreamCodec<FriendlyByteBuf, CommonPacketWrapper<T>> getCodec()
    {
        return CustomPacketPayload.codec(
                (packet, buf) -> this.encoder().accept(packet.packet(), buf),
                (buf) -> new CommonPacketWrapper<>(this, this.decoder().apply(buf)));
    }
}
