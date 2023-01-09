package commonnetwork;

import commonnetwork.api.CommonNetworking;
import commonnetwork.api.Dispatch;
import commonnetwork.networking.PacketRegistrationHandler;
import commonnetwork.networking.data.NetworkHandler;

public record CommonNetwork(PacketRegistrationHandler packetRegistration)
{

    static CommonNetwork INSTANCE;

    public CommonNetwork
    {
        INSTANCE = this;
        new CommonNetworking(packetRegistration);
        new Dispatch(packetRegistration);
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
