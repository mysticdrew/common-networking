package commonnetwork.networking;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

public interface PacketRegistrar
{
    /**
     * @return the side
     */
    Side getSide();

    /**
     * Packet Registration, registers a PLAY packet
     *
     * @param type    - The packet type.
     * @param codec   - The StreamCodec.
     * @param handler - The handler method.
     * @param <T>     - The packet type
     * @return The registrar for chaining registrations.
     */
    <T extends CustomPacketPayload> PacketRegistrar registerPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler);

    /**
     * Packet Registration, registers a CONFIGURATION packet
     *
     * @param type    - The packet type.
     * @param codec   - The StreamCodec.
     * @param handler - The handler method.
     * @param <T>     - The packet type
     * @return The registrar for chaining registrations.
     */
    <T extends CustomPacketPayload> PacketRegistrar registerConfigurationPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler);
}
