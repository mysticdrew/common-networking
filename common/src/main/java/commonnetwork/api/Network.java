package commonnetwork.api;

import commonnetwork.CommonNetworkMod;
import commonnetwork.networking.PacketRegistrar;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Network
{
    /**
     * Packet Registration
     * @deprecated This will likely be removed with the 1.20.5 update due to changes in vanilla and modloader networking.
     * @param packetIdentifier - The unique {@link ResourceLocation} packet id.
     * @param messageType      - The class of the packet.
     * @param encoder          - The encoder method.
     * @param decoder          - The decoder method.
     * @param handler          - The handler method.
     * @param <T>              - The type
     * @return The registrar for chaining registrations.
     */
    @Deprecated
    public static <T> PacketRegistrar registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        return CommonNetworkMod.registerPacket(packetIdentifier, messageType, encoder, decoder, handler);
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
