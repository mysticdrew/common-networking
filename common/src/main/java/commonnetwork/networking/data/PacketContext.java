package commonnetwork.networking.data;

import net.minecraft.server.level.ServerPlayer;

public record PacketContext<T>(ServerPlayer sender, T message, Side side)
{
    public PacketContext(T message, Side side) {
        this(null, message, side);
    }
}
