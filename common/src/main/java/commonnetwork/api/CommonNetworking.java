package commonnetwork.api;

import commonnetwork.CommonNetwork;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommonNetworking
{
    /**
     * Gets the side
     * CLIENT is the client side in
     * SERVER
     * @return the side
     */
    public static Side getSide()
    {
        return CommonNetwork.getInstance().packetRegistration().getSide();
    }

    /**
     * @param packetIdentifier - The unique {@link ResourceLocation} packet id.
     * @param messageType      - The class of the packet.
     * @param encoder          - The encoder method.
     * @param decoder          - The decoder method.
     * @param handler          - The handler method.
     */
    public static <T> void registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        CommonNetwork.getInstance().packetRegistration()
                .registerPacket(packetIdentifier, messageType, encoder, decoder, handler);
    }
}
