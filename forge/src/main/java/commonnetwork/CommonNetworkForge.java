package commonnetwork;


import commonnetwork.networking.ForgeNetworkHandler;
import commonnetwork.networking.data.Side;
import commonnetwork.test_packets.TestRegistration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Constants.MOD_ID)
public class CommonNetworkForge
{
    public CommonNetworkForge()
    {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);
    }

    public void commonSetupEvent(FMLCommonSetupEvent event)
    {
        new CommonNetworkMod(new ForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER));
        new TestRegistration().init();
    }
}
