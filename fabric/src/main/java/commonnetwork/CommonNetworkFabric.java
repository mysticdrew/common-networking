package commonnetwork;

import commonnetwork.networking.FabricNetworkHandler;
import commonnetwork.networking.data.Side;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class CommonNetworkFabric implements ModInitializer
{

    public CommonNetworkFabric()
    {
    }

    @Override
    public void onInitialize()
    {
        var env = FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT) ? Side.CLIENT : Side.SERVER;
        new CommonNetworkMod(new FabricNetworkHandler(env));
    }
}
