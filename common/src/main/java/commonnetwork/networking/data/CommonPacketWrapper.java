package commonnetwork.networking.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CommonPacketWrapper<T>(PacketContainer<T> container, T packet) implements CustomPacketPayload
{
    public void encode(FriendlyByteBuf buf)
    {
        container().encoder().accept(packet(), buf);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return container.type();
    }
}
