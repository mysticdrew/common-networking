package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FabricNetworkHandler extends PacketRegistrationHandler
{
    private final Map<Class<?>, Message<?>> MESSAGE_MAP = new HashMap<>();

    public FabricNetworkHandler()
    {
        super();
    }

    protected <T> void registerPacket(PacketContainer<T> container)
    {
        MESSAGE_MAP.put(container.messageType(), new Message<>(container.packetIdentifier(), container.encoder()));
        if (EnvType.CLIENT.equals(FabricLoader.getInstance().getEnvironmentType()))
        {
            Constants.LOG.info("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), Side.CLIENT);

            ClientPlayNetworking.registerGlobalReceiver(container.packetIdentifier(), ((client, listener, buf, responseSender) -> {
                buf.readByte(); // handle forge discriminator
                T message = container.decoder().apply(buf);
                client.execute(() -> container.handler().accept(new PacketContext<>(message, Side.CLIENT)));
            }));
        }
        else
        {
            Constants.LOG.info("Registering packet {} : {} on the: {}", container.packetIdentifier(), container.messageType(), Side.SERVER);

            ServerPlayNetworking.registerGlobalReceiver(container.packetIdentifier(), ((server, player, listener, buf, responseSender) -> {
                buf.readByte(); // handle forge discriminator
                T message = container.decoder().apply(buf);
                server.execute(() -> container.handler().accept(new PacketContext<>(player, message, Side.SERVER)));
            }));
        }
    }

    public <T> void sendToServer(T packet)
    {
        this.sendToServer(packet, false);
    }

    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        Message<T> message = (Message<T>) MESSAGE_MAP.get(packet.getClass());
        if (ClientPlayNetworking.canSend(message.id()) || ignoreCheck)
        {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeByte(0); // handle forge discriminator
            message.encoder().accept(packet, buf);
            ClientPlayNetworking.send(message.id(), buf);
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        this.sendToClient(packet, player, false);
    }

    public <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        Message<T> message = (Message<T>) MESSAGE_MAP.get(packet.getClass());
        if (ServerPlayNetworking.canSend(player, message.id()) || ignoreCheck)
        {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeByte(0); // handle forge discriminator
            message.encoder().accept(packet, buf);
            ServerPlayNetworking.send(player, message.id(), buf);
        }
    }

    public record Message<T>(ResourceLocation id, BiConsumer<T, FriendlyByteBuf> encoder)
    {
    }
}
