package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.Consumer;


public class NeoForgeNetworkHandler extends PacketRegistrationHandler
{

    public NeoForgeNetworkHandler(Side side)
    {
        super(side);
    }

    @Override
    protected <T extends CustomPacketPayload> void onRegister(PacketContainer<T> container)
    {
        // NeoForge defers registration to RegisterPayloadHandlersEvent below.
    }

    @SubscribeEvent
    public void register(final RegisterPayloadHandlersEvent event)
    {
        PACKET_MAP.values().forEach(container -> registerOne(event, container));
    }

    @SuppressWarnings("unchecked")
    private <T extends CustomPacketPayload> void registerOne(RegisterPayloadHandlersEvent event, PacketContainer<T> container)
    {
        PayloadRegistrar registrar = event.registrar(container.type().id().getNamespace()).optional();
        IPayloadHandler<T> handler = buildHandler(container.handler());
        if (container.packetType() == PacketContainer.PacketType.PLAY)
        {
            registrar.playBidirectional(container.type(), (StreamCodec<? super RegistryFriendlyByteBuf, T>) container.codec(), handler, handler);
        }
        else
        {
            registrar.configurationBidirectional(container.type(), (StreamCodec<? super FriendlyByteBuf, T>) container.codec(), handler, handler);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T packet, boolean ignoreCheck)
    {
        PacketContainer<T> container = requireContainer(packet);
        if (ignoreCheck || Minecraft.getInstance().getConnection().hasChannel(container.type()))
        {
            Minecraft.getInstance().getConnection().getConnection().send(new ServerboundCustomPayloadPacket(packet));
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        PacketContainer<T> container = requireContainer(packet);
        if (ignoreCheck || player.connection.hasChannel(container.type()))
        {
            player.connection.getConnection().send(new ClientboundCustomPayloadPacket(packet));
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
            else if (this.side == Side.CLIENT)
            {
                connection.send(new ServerboundCustomPayloadPacket(packet));
            }
        }
    }

    private <T extends CustomPacketPayload> IPayloadHandler<T> buildHandler(Consumer<PacketContext<T>> handler)
    {
        return (payload, ctx) -> {
            try
            {
                Side packetSide = ctx.flow().getReceptionSide().equals(LogicalSide.SERVER) ? Side.SERVER : Side.CLIENT;
                ServerPlayer player = ctx.player() instanceof ServerPlayer sp ? sp : null;
                handler.accept(new PacketContext<>(player, payload, packetSide));
            }
            catch (Throwable t)
            {
                Constants.LOG.error("Error handling packet: {} -> ", payload.getClass(), t);
            }
        };
    }
}
