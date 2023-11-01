package commonnetwork;


import commonnetwork.networking.NeoForgeNetworkHandler;
import commonnetwork.networking.data.Side;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLLoader;


@Mod(Constants.MOD_ID)
public class CommonNetworkNeoForge
{
    public CommonNetworkNeoForge()
    {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);
    }

    public void commonSetupEvent(FMLCommonSetupEvent event)
    {
        new CommonNetworkMod(new NeoForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER));
    }
}
