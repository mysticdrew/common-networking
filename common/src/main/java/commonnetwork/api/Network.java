package commonnetwork.api;

import commonnetwork.CommonNetworkMod;
import commonnetwork.networking.PacketRegistrar;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Network
{
    /**
     * Packet Registration
     *
     * @param packetIdentifier - The unique {@link ResourceLocation} packet id.
     * @param packetClass      - The class of the packet.
     * @param encoder          - The encoder method.
     * @param decoder          - The decoder method.
     * @param handler          - The handler method.
     * @param <T>              - The type
     * @return The registrar for chaining registrations.
     * @deprecated this method will eventually be removed, please migrate to the method supplying your own encoding/decoding codec. Likely in 1.21.2 or 1.22.
     */
    @Deprecated(forRemoval = true)
    public static <T> PacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> packetClass, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        return CommonNetworkMod.registerPacket(packetIdentifier, packetClass, encoder, decoder, handler);
    }

    /**
     * Packet Registration
     *
     * @param type        - The packet type.
     * @param packetClass - The class of the packet.
     * @param codec       - The StreamCodec.
     * @param handler     - The handler method.
     * @param <T>         - The class type
     * @return The registrar for chaining registrations.
     */
    public static <T, B extends FriendlyByteBuf> PacketRegistrar registerPacket(CustomPacketPayload.Type<? extends CustomPacketPayload> type, Class<T> packetClass, StreamCodec<B, T> codec, Consumer<PacketContext<T>> handler)
    {
        return CommonNetworkMod.registerPacket(type, packetClass, codec, handler);
    }

    /**
     * Gets the Network handler for use to send packets.
     *
     * @return - The network handler
     */
    public static NetworkHandler getNetworkHandler()
    {
        return CommonNetworkMod.INSTANCE.getPacketRegistration();
    }
}
