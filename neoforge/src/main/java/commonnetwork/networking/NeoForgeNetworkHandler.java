package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


public class NeoForgeNetworkHandler extends PacketRegistrationHandler
{


    private final Map<Class<?>, NeoForgePacketContainer> PACKETS = new HashMap<>();

    public NeoForgeNetworkHandler(Side side)
    {
        super(side);
    }

    @SubscribeEvent
    public void register(final RegisterPayloadHandlerEvent event)
    {
        if (!PACKETS.isEmpty())
        {
            PACKETS.forEach((type, container) -> {
                System.out.println("Packet Registration :"+ type.toGenericString());
                final IPayloadRegistrar registrar = event.registrar(container.packetIdentifier().getNamespace());
                registrar.optional().common(
                        container.packetIdentifier(),
                        container.decoder(),
                        container.handler());
            });
        }
    }

    protected <T> void registerPacket(PacketContainer<T> container)
    {
        if (PACKETS.get(container.messageType()) == null)
        {
            var packetContainer = new NeoForgePacketContainer<>(
                    container.messageType(),
                    container.packetIdentifier(),
                    encoder(container.encoder()),
                    decoder(container.decoder()),
                    buildHandler(container.handler())
            );

            PACKETS.put(container.messageType(), packetContainer);
        }
    }

    private <T> BiConsumer<T, FriendlyByteBuf> encoder(BiConsumer<T, FriendlyByteBuf> encoder)
    {
        return (e, c) -> {
            c.writeByte(0);
            encoder.accept(e, c);
        };
    }

    private <T> FriendlyByteBuf.Reader<NeoForgePacket<T>> decoder(Function<FriendlyByteBuf, T> decoder)
    {
        return (buf -> {
            buf.readByte();
            T packet = decoder.apply(buf);
            return new NeoForgePacket<T>(PACKETS.get(packet.getClass()), packet);
        });
    }

    public <T> void sendToServer(T packet)
    {
        this.sendToServer(packet, false);
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        NeoForgePacketContainer<T> container = PACKETS.get(packet.getClass());
        try
        {
            PacketDistributor.SERVER.noArg().send(new NeoForgePacket<>(container, packet));

        }
        catch (Throwable t)
        {
            Constants.LOG.error("{} packet not registered on the client, this is needed.", packet.getClass(), t);
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        NeoForgePacketContainer<T> container = PACKETS.get(packet.getClass());
        try
        {
            if (player.connection.isConnected(container.packetIdentifier()))
            {

                PacketDistributor.PLAYER.with(player).send(new NeoForgePacket<>(container, packet));
            }
        }
        catch (Throwable t)
        {
            Constants.LOG.error("{} packet not registered on the server, this is needed.", packet.getClass(), t);
        }
    }

    private <T, K extends
            NeoForgePacket<T>> IPayloadHandler<K> buildHandler(Consumer<PacketContext<T>> handler)
    {
        return (payload, ctx) -> {
            try
            {
                Side side = ctx.flow().getReceptionSide().equals(LogicalSide.SERVER) ? Side.SERVER : Side.CLIENT;
                Player player = ctx.player().orElse(null);
                handler.accept(new PacketContext<>(player, payload.packet(), side));
            }
            catch (Throwable t)
            {
                Constants.LOG.error("Error handling packet: {} -> ", payload.packet().getClass(), t);
            }
        };
    }
}
