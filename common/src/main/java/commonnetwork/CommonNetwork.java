package commonnetwork;

import commonnetwork.networking.PacketRegistrationHandler;
import commonnetwork.networking.data.NetworkHandler;

public record CommonNetwork(PacketRegistrationHandler packetRegistration)
{

    static CommonNetwork INSTANCE;

    public CommonNetwork
    {
        INSTANCE = this;
    }

    public static CommonNetwork getInstance()
    {
        return INSTANCE;
    }

    public NetworkHandler getNetworkHandler()
    {
        return packetRegistration;
    }
}
