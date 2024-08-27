package commonnetwork.networking.data;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

//TODO: Removing for mc 1.21.2 or 1.22
@Deprecated(forRemoval = true)
public record CommonPacketWrapper<T>(PacketContainer<T> container, T packet) implements CustomPacketPayload
{
    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return container.type();
    }
}
