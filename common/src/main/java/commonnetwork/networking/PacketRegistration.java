package commonnetwork.networking;

import commonnetwork.networking.data.PacketContainer;
import commonnetwork.networking.data.Side;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistration
{

    private final Side side;

    public PacketRegistration(Side side)
    {
        this.side = side;
    }

    private final Map<Class<?>, PacketContainer<?>> packets = new HashMap<>();



    public Side getSide()
    {
        return this.side;
    }
}
