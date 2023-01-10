package commonnetwork;

import commonnetwork.networking.PacketRegistrationHandler;

public record CommonNetworkMod(PacketRegistrationHandler packetRegistration)
{

    public static CommonNetworkMod INSTANCE;

    public CommonNetworkMod
    {
        INSTANCE = this;
    }
}
