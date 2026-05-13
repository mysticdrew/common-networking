package commonnetwork;

import commonnetwork.networking.DelayedPacketRegistrationHandler;
import commonnetwork.networking.PacketRegistrar;
import commonnetwork.networking.PacketRegistrationHandler;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

public class CommonNetworkMod
{
    private final PacketRegistrationHandler packetRegistration;
    private static DelayedPacketRegistrationHandler delayedHandler;
    public static CommonNetworkMod INSTANCE;

    public CommonNetworkMod(PacketRegistrationHandler packetRegistration)
    {
        INSTANCE = this;
        this.packetRegistration = packetRegistration;
        getDelayedHandler().registerQueuedPackets(packetRegistration);
    }

    /**
     * Fabric does not enforce load order, so we may have to delay packet registrations.
     *
     * @return the handler;
     */
    private static DelayedPacketRegistrationHandler getDelayedHandler()
    {
        if (delayedHandler == null)
        {
            delayedHandler = new DelayedPacketRegistrationHandler();
        }
        return delayedHandler;
    }

    public static <T extends CustomPacketPayload> PacketRegistrar registerPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        if (INSTANCE != null)
        {
            return INSTANCE.packetRegistration.registerPacket(type, codec, handler);
        }
        return getDelayedHandler().registerPacket(type, codec, handler);
    }

    public static <T extends CustomPacketPayload> PacketRegistrar registerConfigurationPacket(CustomPacketPayload.Type<T> type, StreamCodec<? extends FriendlyByteBuf, T> codec, Consumer<PacketContext<T>> handler)
    {
        if (INSTANCE != null)
        {
            return INSTANCE.packetRegistration.registerConfigurationPacket(type, codec, handler);
        }
        return getDelayedHandler().registerConfigurationPacket(type, codec, handler);
    }

    public PacketRegistrationHandler getPacketRegistration()
    {
        return packetRegistration;
    }
}
