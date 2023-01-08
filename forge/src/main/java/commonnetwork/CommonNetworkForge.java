package commonnetwork;


import commonnetwork.networking.ForgeNetworkHandler;
import commonnetwork.test_packets.TestRegistration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class CommonNetworkForge
{
    public CommonNetworkForge()
    {
        new CommonNetwork(new ForgeNetworkHandler());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);
    }

    public void commonSetupEvent(FMLCommonSetupEvent event)
    {
        new TestRegistration().init();
    }
}
