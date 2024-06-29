package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;

public class ForgeNetworkHandler extends PacketRegistrationHandler
{
    private final Map<Class<?>, SimpleChannel> CHANNELS = new HashMap<>();

    public ForgeNetworkHandler(Side side)
    {
        super(side);
    }

    protected <T> void registerPacket(PacketContainer<T> container)
    {
        if (CHANNELS.get(container.messageType()) == null)
        {
            SimpleChannel channel = NetworkRegistry.ChannelBuilder
                    .named(container.packetIdentifier())
                    .clientAcceptedVersions((a) -> true)
                    .serverAcceptedVersions((a) -> true)
                    .networkProtocolVersion(() -> "1")
                    .simpleChannel();
            try
            {
                channel.registerMessage(0, container.messageType(), container.encoder(), container.decoder(), buildHandler(container.handler()));
                Constants.LOG.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), this.side);
                CHANNELS.put(container.messageType(), channel);
            }
            catch (IllegalArgumentException iae)
            {
                Constants.LOG.error("Packet \"{}\" likely already registered {} on :{}", container.packetIdentifier(), container.messageType(), this.side, iae);
            }
        }
    }

    public <T> void sendToServer(T packet)
    {
        this.sendToServer(packet, false);
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        if (channel != null)
        {
            Connection connection = Minecraft.getInstance().getConnection().getConnection();
            if (channel.isRemotePresent(connection) || ignoreCheck)
            {
                channel.sendToServer(packet);
            }

        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the client, packets need to be registered don both sides!");
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        if (channel != null)
        {
            Connection connection = player.connection.connection;
            if (channel.isRemotePresent(connection))
            {
                channel.sendTo(packet, player.connection.connection, PLAY_TO_CLIENT);
            }
        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the server, packets need to be registered don both sides!");
        }
    }


    private <
            T> BiConsumer<T, Supplier<NetworkEvent.Context>> buildHandler(Consumer<PacketContext<T>> handler)
    {
        return (message, ctx) -> {
            ctx.get().enqueueWork(() -> {
                Side side = ctx.get().getDirection().getReceptionSide().isServer() ? Side.SERVER : Side.CLIENT;
                ServerPlayer player = ctx.get().getSender();
                handler.accept(new PacketContext<>(player, message, side));
            });
            ctx.get().setPacketHandled(true);
        };
    }
}
