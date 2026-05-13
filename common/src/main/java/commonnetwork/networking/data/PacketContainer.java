package commonnetwork.networking.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

public record PacketContainer<T extends CustomPacketPayload>(
        CustomPacketPayload.Type<T> type,
        StreamCodec<? super FriendlyByteBuf, T> codec,
        Consumer<PacketContext<T>> handler,
        PacketType packetType)
{
    public enum PacketType
    {
        PLAY,
        CONFIGURATION
    }
}
