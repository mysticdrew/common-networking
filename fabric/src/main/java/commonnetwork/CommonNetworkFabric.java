package commonnetwork;

import commonnetwork.networking.FabricNetworkHandler;
import commonnetwork.test_packets.TestRegistration;
import net.fabricmc.api.ModInitializer;

public class CommonNetworkFabric implements ModInitializer
{

    public CommonNetworkFabric()
    {
        new CommonNetwork(new FabricNetworkHandler());
    }

    @Override
    public void onInitialize()
    {
        new TestRegistration().init();
    }
}
