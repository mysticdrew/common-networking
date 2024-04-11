package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FabricNetworkHandler extends PacketRegistrationHandler
{
    private final Map<Class<?>, Message<?>> CHANNELS = new HashMap<>();

    public FabricNetworkHandler(Side side)
    {
        super(side);
    }

    protected <T> void registerPacket(PacketContainer<T> container)
    {
        if (CHANNELS.get(container.messageType()) == null)
        {
            CHANNELS.put(container.messageType(), new Message<>(container.packetIdentifier(), container.encoder()));
            if (Side.CLIENT.equals(this.side))
            {
                Constants.LOG.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), Side.CLIENT);

                ClientPlayNetworking.registerGlobalReceiver(container.packetIdentifier(), ((client, listener, buf, responseSender) -> {
                    buf.readByte(); // handle forge discriminator
                    T message = container.decoder().apply(buf);
                    client.execute(() -> container.handler().accept(new PacketContext<>(message, Side.CLIENT)));
                }));
            }
            else
            {
                Constants.LOG.debug("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), Side.SERVER);

                ServerPlayNetworking.registerGlobalReceiver(container.packetIdentifier(), ((server, player, listener, buf, responseSender) -> {
                    buf.readByte(); // handle forge discriminator
                    T message = container.decoder().apply(buf);
                    server.execute(() -> container.handler().accept(new PacketContext<>(player, message, Side.SERVER)));
                }));
            }
        }
    }

    public <T> void sendToServer(T packet)
    {
        this.sendToServer(packet, false);
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        Message<T> message = (Message<T>) CHANNELS.get(packet.getClass());
        if (message != null)
        {
            if (ClientPlayNetworking.canSend(message.id()) || ignoreCheck)
            {
                FriendlyByteBuf buf = PacketByteBufs.create();
                buf.writeByte(0); // handle forge discriminator
                message.encoder().accept(packet, buf);
                ClientPlayNetworking.send(message.id(), buf);
            }
        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the client, packets need to be registered don both sides!");
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        Message<T> message = (Message<T>) CHANNELS.get(packet.getClass());
        if (message != null)
        {
            if (ServerPlayNetworking.canSend(player, message.id()))
            {
                FriendlyByteBuf buf = PacketByteBufs.create();
                buf.writeByte(0); // handle forge discriminator
                message.encoder().accept(packet, buf);
                ServerPlayNetworking.send(player, message.id(), buf);
            }
        }
        else
        {
            throw new RegistrationException(packet.getClass() + "{} packet not registered on the server, packets need to be registered don both sides!");
        }
    }

    public record Message<T>(ResourceLocation id, BiConsumer<T, FriendlyByteBuf> encoder)
    {
    }
}
