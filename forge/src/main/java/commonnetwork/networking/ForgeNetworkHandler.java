package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

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
            SimpleChannel channel = ChannelBuilder
                    .named(container.packetIdentifier())
                    .clientAcceptedVersions((a, b) -> true)
                    .serverAcceptedVersions((a, b) -> true)
                    .networkProtocolVersion(1)
                    .simpleChannel();

            channel.messageBuilder(container.messageType())
                    .decoder(container.decoder())
                    .encoder(container.encoder())
                    .consumerNetworkThread(buildHandler(container.handler()))
                    .add();
            Constants.LOG.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), this.side);
            CHANNELS.put(container.messageType(), channel);
        }
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
            channel.send(packet, PacketDistributor.SERVER.noArg());
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = player.connection.getConnection();
        if (channel.isRemotePresent(connection))
        {
            channel.send(packet, PacketDistributor.PLAYER.with(player));
        }
    }


    private <T> BiConsumer<T, CustomPayloadEvent.Context> buildHandler(Consumer<PacketContext<T>> handler)
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
