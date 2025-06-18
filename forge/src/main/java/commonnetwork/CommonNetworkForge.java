package commonnetwork;


import commonnetwork.networking.ForgeNetworkHandler;
import commonnetwork.networking.data.Side;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Constants.MOD_ID)
public class CommonNetworkForge
{
    public CommonNetworkForge(FMLJavaModLoadingContext context)
    {
        FMLCommonSetupEvent.getBus(context.getModBusGroup()).addListener(this::commonSetupEvent);
    }

    public void commonSetupEvent(FMLCommonSetupEvent event)
    {
        new CommonNetworkMod(new ForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER));
    }
}
