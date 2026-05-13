package commonnetwork.networking;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import commonnetwork.networking.exceptions.RegistrationException;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class PaperNetworkHandler extends PacketRegistrationHandler implements PluginMessageListener
{
    private final JavaPlugin plugin;
    private final Set<Identifier> registeredChannels = new HashSet<>();

    public PaperNetworkHandler(JavaPlugin plugin, Side side)
    {
        super(side);
        this.plugin = plugin;
    }

    @Override
    protected boolean supports(PacketContainer.PacketType type)
    {
        return type == PacketContainer.PacketType.PLAY;
    }

    @Override
    protected <T> void registerPacket(PacketContainer<T> container)
    {
        Identifier id = container.type().id();
        if (!registeredChannels.add(id))
        {
            return;
        }
        String channel = id.toString();
        Messenger messenger = plugin.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(plugin, channel);
        messenger.registerIncomingPluginChannel(plugin, channel, this);
    }

    public void shutdown()
    {
        Messenger messenger = plugin.getServer().getMessenger();
        for (Identifier id : registeredChannels)
        {
            String channel = id.toString();
            messenger.unregisterOutgoingPluginChannel(plugin, channel);
            messenger.unregisterIncomingPluginChannel(plugin, channel, this);
        }
        registeredChannels.clear();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message)
    {
        Identifier id = Identifier.tryParse(channel);
        if (id == null)
        {
            return;
        }
        PacketContainer<?> container = getPacketContainer(id);
        if (container == null)
        {
            return;
        }
        ServerPlayer sender = ((CraftPlayer) player).getHandle();
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(message), sender.registryAccess());
        Object packet;
        try
        {
            packet = container.decode(buf);
        }
        catch (Exception e)
        {
            plugin.getLogger().log(Level.WARNING, "Failed to decode packet on channel " + channel, e);
            return;
        }
        plugin.getServer().getScheduler().runTask(plugin, () -> dispatch(container, sender, packet));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void dispatch(PacketContainer container, ServerPlayer sender, Object packet)
    {
        container.handler().accept(new PacketContext(sender, packet, Side.SERVER));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        PacketContainer<T> container = (PacketContainer<T>) getPacketContainer(packet.getClass());
        try
        {
            if (container == null)
            {
                throw new RegistrationException(packet.getClass()
                        + " packet not registered on the server, packets need to be registered on both sides!");
            }
            Identifier id = container.type().id();
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
            container.encode(buf, packet);
            player.connection.send(new ClientboundCustomPayloadPacket(new DiscardedPayload(id, ByteBufUtil.getBytes(buf))));
        }
        catch (Throwable t)
        {
            Constants.LOG.error("Error sending packet {}", container != null ? container.toString() : "unknown", t);
        }
    }

    @Override
    public <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        throw new UnsupportedOperationException(
                "PaperNetworkHandler runs on the server side; sendToServer is not supported.");
    }

    @Override
    public <T> void send(T packet, Connection connection)
    {
        throw new UnsupportedOperationException(
                "PaperNetworkHandler does not support raw Connection sends; use sendToClient(packet, player).");
    }

    @Override
    public <T> @Nullable ClientboundCustomPayloadPacket getRawClientboundPacket(T packet)
    {
        throw new UnsupportedOperationException(
                "PaperNetworkHandler does not produce raw ClientboundCustomPayloadPacket; the Bukkit messenger handles framing.");
    }

    @Override
    public <T> @Nullable ServerboundCustomPayloadPacket getRawServerboundPacket(T packet)
    {
        throw new UnsupportedOperationException(
                "PaperNetworkHandler is server-side; serverbound raw packets are not produced here.");
    }
}
