package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHandler extends PacketRegistrationHandler
{

    public FabricNetworkHandler(Side side)
    {
        super(side);
    }

    @Override
    protected <T extends CustomPacketPayload> void onRegister(PacketContainer<T> container)
    {
        try
        {
            if (container.packetType() == PacketContainer.PacketType.PLAY)
            {
                PayloadTypeRegistry.serverboundPlay().register(container.type(), container.codec());
                PayloadTypeRegistry.clientboundPlay().register(container.type(), container.codec());
            }
            else
            {
                PayloadTypeRegistry.serverboundConfiguration().register(container.type(), container.codec());
                PayloadTypeRegistry.clientboundConfiguration().register(container.type(), container.codec());
            }
        }
        catch (IllegalArgumentException ignored)
        {
            // already registered
        }

        if (Side.CLIENT.equals(this.side))
        {
            Constants.LOG.debug("Registering packet {} on the: {}", container.type().id(), Side.CLIENT);

            if (container.packetType() == PacketContainer.PacketType.PLAY)
            {
                ClientPlayNetworking.registerGlobalReceiver(container.type(),
                        (payload, context) -> context.client().execute(() ->
                                container.handler().accept(new PacketContext<>(payload, Side.CLIENT))));
            }
            else
            {
                ClientConfigurationNetworking.registerGlobalReceiver(container.type(),
                        (payload, context) -> context.client().execute(() ->
                                container.handler().accept(new PacketContext<>(payload, Side.CLIENT))));
            }
        }

        Constants.LOG.debug("Registering packet {} on the: {}", container.type().id(), Side.SERVER);
        if (container.packetType() == PacketContainer.PacketType.PLAY)
        {
            ServerPlayNetworking.registerGlobalReceiver(container.type(),
                    (payload, context) -> context.player().level().getServer().execute(() ->
                            container.handler().accept(new PacketContext<>(context.player(), payload, Side.SERVER))));
        }
        else
        {
            ServerConfigurationNetworking.registerGlobalReceiver(container.type(),
                    (payload, context) -> context.server().execute(() ->
                            container.handler().accept(new PacketContext<>(null, payload, Side.SERVER))));
        }
    }

    @Override
    public <T extends CustomPacketPayload> void send(T packet, Connection connection)
    {
        if (PACKET_MAP.containsKey(packet.type()))
        {
            if (this.side == Side.SERVER)
            {
                connection.send(new ClientboundCustomPayloadPacket(packet));
            }
            else
            {
                connection.send(new ServerboundCustomPayloadPacket(packet));
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T packet, boolean ignoreCheck)
    {
        PacketContainer<T> container = requireContainer(packet);
        if (ignoreCheck || ClientPlayNetworking.canSend(container.type().id()))
        {
            ClientPlayNetworking.send(packet);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        PacketContainer<T> container = requireContainer(packet);
        if (ignoreCheck || ServerPlayNetworking.canSend(player, container.type().id()))
        {
            ServerPlayNetworking.send(player, packet);
        }
    }
}
