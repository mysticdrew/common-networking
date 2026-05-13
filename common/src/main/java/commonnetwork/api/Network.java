package commonnetwork.api;

import commonnetwork.CommonNetworkMod;
import commonnetwork.networking.PacketRegistrar;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

public class Network
{
    /**
     * Packet Registration
     *
     * @param type    - The packet type.
     * @param codec   - The StreamCodec.
     * @param handler - The handler method.
     * @param <T>     - The packet type
     * @return The registrar for chaining registrations.
     */
    public static <T extends CustomPacketPayload> PacketRegistrar registerPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        return CommonNetworkMod.registerPacket(type, codec, handler);
    }

    /**
     * Packet Registration for the CONFIGURATION phase.
     *
     * @param type    - The packet type.
     * @param codec   - The StreamCodec.
     * @param handler - The handler method.
     * @param <T>     - The packet type
     * @return The registrar for chaining registrations.
     */
    public static <T extends CustomPacketPayload> PacketRegistrar registerConfigurationPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        return CommonNetworkMod.registerConfigurationPacket(type, codec, handler);
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
