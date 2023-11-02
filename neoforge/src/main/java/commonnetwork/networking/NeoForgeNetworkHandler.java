package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.MessageFunctions;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


public class NeoForgeNetworkHandler extends PacketRegistrationHandler
{
    private final Map<Class<?>, SimpleChannel> CHANNELS = new HashMap<>();

    public NeoForgeNetworkHandler(Side side)
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
            channel.registerMessage(0, container.messageType(), encoder(container.encoder()), decoder(container.decoder()), buildHandler(container.handler()));
            Constants.LOG.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), this.side);
            CHANNELS.put(container.messageType(), channel);
        }
    }

    static <T> MessageFunctions.MessageEncoder<T> encoder(BiConsumer<T, FriendlyByteBuf> encoder) {
        return encoder::accept;
    }

    static <T> MessageFunctions.MessageDecoder<T> decoder(Function<FriendlyByteBuf, T> decoder) {
        return decoder::apply;
    }

    public <T> void sendToServer(T packet)
    {
        this.sendToServer(packet, false);
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        if (channel.isRemotePresent(connection) || ignoreCheck)
        {
            channel.sendToServer(packet);
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = player.connection.connection;
        if (channel.isRemotePresent(connection))
        {
            channel.sendTo(packet, player.connection.connection, PlayNetworkDirection.PLAY_TO_CLIENT);
        }
    }


    private <T> MessageFunctions.MessageConsumer<T> buildHandler(Consumer<PacketContext<T>> handler)
    {
        return (message, ctx) -> {
            ctx.enqueueWork(() -> {
                Side side = ctx.getDirection().getReceptionSide().isServer() ? Side.SERVER : Side.CLIENT;
                ServerPlayer player = ctx.getSender();
                handler.accept(new PacketContext<>(player, message, side));
            });
            ctx.setPacketHandled(true);
        };
    }
}
