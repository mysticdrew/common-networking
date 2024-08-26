package commonnetwork.networking.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

//TODO: Removing for mc 1.21.2 or 1.22
@Deprecated(forRemoval = true)
public record CommonPacketWrapper<T, B extends FriendlyByteBuf>(PacketContainer<T, B> container, T packet) implements CustomPacketPayload
{
    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return container.type();
    }
}
