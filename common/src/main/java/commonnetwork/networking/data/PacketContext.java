package commonnetwork.networking.data;

import net.minecraft.server.level.ServerPlayer;

public final class PacketContext<T>
{
    private final ServerPlayer sender;
    private final T message;
    private final Side side;

    public PacketContext(ServerPlayer sender, T message, Side side)
    {
        this.sender = sender;
        this.message = message;
        this.side = side;
    }

    public PacketContext(T message, Side side)
    {
        this(null, message, side);
    }

    public ServerPlayer sender()
    {
        return sender;
    }

    public T message()
    {
        return message;
    }

    public Side side()
    {
        return side;
    }
}
