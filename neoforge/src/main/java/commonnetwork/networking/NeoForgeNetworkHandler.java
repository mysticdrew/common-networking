package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.CommonPacketWrapper;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.function.Consumer;


public class NeoForgeNetworkHandler extends PacketRegistrationHandler
{


    public NeoForgeNetworkHandler(Side side)
    {
        super(side);
    }

    @Override
    <T, B extends FriendlyByteBuf> void registerPacket(PacketContainer<T, B> container)
    {
        // not needed for neoforge
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public void register(final RegisterPayloadHandlersEvent event)
    {
        if (!PACKET_MAP.isEmpty())
        {
            PACKET_MAP.forEach((type, container) -> event.registrar(container.getType().id().getNamespace())
                    .optional().commonBidirectional(container.getType(), container.getCodec(), buildHandler(container.handler())));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        PacketContainer<T, ?> container = (PacketContainer<T, ?>) PACKET_MAP.get(packet.getClass());
        if (container != null)
        {
            if (ignoreCheck || Minecraft.getInstance().getConnection().hasChannel(container.getType()))
            {
                Minecraft.getInstance().getConnection()
                        .send(new ServerboundCustomPayloadPacket(new CommonPacketWrapper<>(container, packet)));
            }
        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the client, packets need to be registered on both sides!");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        PacketContainer<T, ?> container = (PacketContainer<T, ?>) PACKET_MAP.get(packet.getClass());
        if (container != null)
        {
            if (ignoreCheck || player.connection.hasChannel(container.type()))
            {
                PacketDistributor.sendToPlayer(player, new CommonPacketWrapper<>(container, packet));
            }
        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the server, packets need to be registered on both sides!");
        }
    }

    private <T, K extends
            CommonPacketWrapper<T, ?>> IPayloadHandler<K> buildHandler(Consumer<PacketContext<T>> handler)
    {
        return (payload, ctx) -> {
            try
            {
                Side side = ctx.flow().getReceptionSide().equals(LogicalSide.SERVER) ? Side.SERVER : Side.CLIENT;
                if (Side.SERVER.equals(side))
                {
                    handler.accept(new PacketContext<>((ServerPlayer) ctx.player(), payload.packet(), side));
                }
                else
                {
                    handler.accept(new PacketContext<>(payload.packet(), side));
                }

            }
            catch (Throwable t)
            {
                Constants.LOG.error("Error handling packet: {} -> ", payload.packet().getClass(), t);
            }
        };
    }
}
