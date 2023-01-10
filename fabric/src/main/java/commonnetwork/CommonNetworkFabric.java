package commonnetwork;

import commonnetwork.networking.FabricNetworkHandler;
import commonnetwork.networking.data.Side;
import commonnetwork.test_packets.TestRegistration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class CommonNetworkFabric implements ModInitializer, ClientModInitializer
{

    public CommonNetworkFabric()
    {
    }

    @Override
    public void onInitialize()
    {

        new CommonNetworkMod(new FabricNetworkHandler(Side.SERVER));

    }

    @Override
    public void onInitializeClient()
    {
        new TestRegistration().init();
        new CommonNetworkMod(new FabricNetworkHandler(Side.CLIENT));
//        new TestRegistration().init();
    }
}
