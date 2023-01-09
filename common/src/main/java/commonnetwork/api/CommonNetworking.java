package commonnetwork.api;

import commonnetwork.networking.PacketRegistrationHandler;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommonNetworking
{
    private static CommonNetworking INSTANCE;
    private final PacketRegistrationHandler handler;

    public CommonNetworking(PacketRegistrationHandler handler)
    {
        INSTANCE = this;
        this.handler = handler;
    }

    private static CommonNetworking getInstance()
    {
        if (INSTANCE != null)
        {
            return INSTANCE;
        }
        throw new ExceptionInInitializerError("Common Networking is not initialized!");
    }

    /**
     * Gets the side
     * CLIENT is the client side in
     * SERVER
     *
     * @return the side
     */
    public static Side getSide()
    {
        return getInstance().handler.getSide();
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
        getInstance().handler.registerPacket(packetIdentifier, messageType, encoder, decoder, handler);
    }
}
