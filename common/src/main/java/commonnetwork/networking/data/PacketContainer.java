package commonnetwork.networking.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public record PacketContainer<T, B extends FriendlyByteBuf>(
        CustomPacketPayload.Type<? extends CustomPacketPayload> type,
        Class<T> classType,
        BiConsumer<T, FriendlyByteBuf> encoder,
        Function<FriendlyByteBuf, T> decoder,
        StreamCodec<? super B, T> codec,
        Consumer<PacketContext<T>> handler)
{
    //TODO: Removing for mc 1.21.2 or 1.22
    @Deprecated(forRemoval = true)
    public PacketContainer(ResourceLocation id,
                           Class<T> classType,
                           BiConsumer<T, FriendlyByteBuf> encoder,
                           Function<FriendlyByteBuf, T> decoder,
                           Consumer<PacketContext<T>> handle)
    {
        this(new CustomPacketPayload.Type<>(id), classType, encoder, decoder, null, handle);
    }

    public PacketContainer( CustomPacketPayload.Type<? extends CustomPacketPayload> type,
                           Class<T> classType,
                           StreamCodec<? super B, T> codec,
                           Consumer<PacketContext<T>> handle)
    {
        this(type, classType, null, null, codec, handle);
    }

    @SuppressWarnings("unchecked")
    public <K extends CustomPacketPayload> CustomPacketPayload.Type<K> getType()
    {
        return (CustomPacketPayload.Type<K>) type();
    }


    //TODO: Removing for mc 1.21.2 or 1.22, will also be able to remove the wrapping of the decoder.
    public <K extends FriendlyByteBuf> StreamCodec<K, CommonPacketWrapper> getCodec()
    {
        if (this.codec() == null)
        {

            return CustomPacketPayload.codec(
                    (packet, buf) -> this.encoder().accept((T) packet.packet(), buf),
                    (buf) -> new CommonPacketWrapper<>(this, this.decoder().apply(buf)));
        }
        else
        {
            return CustomPacketPayload.codec(

                    (packet, buf) -> this.codec().encode((B) buf, (T) packet.packet()),
                    (buf) -> new CommonPacketWrapper<>(this, this.codec().decode((B) buf)));
        }
    }
}
