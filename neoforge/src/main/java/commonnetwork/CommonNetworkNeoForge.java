package commonnetwork;


import commonnetwork.networking.NeoForgeNetworkHandler;
import commonnetwork.networking.PacketRegistrationHandler;
import commonnetwork.networking.data.Side;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLLoader;


@Mod(Constants.MOD_ID)
public class CommonNetworkNeoForge
{
    private final PacketRegistrationHandler handler;

    public CommonNetworkNeoForge()
    {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);
        handler = new NeoForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER);
        FMLJavaModLoadingContext.get().getModEventBus().register(handler);
    }

    public void commonSetupEvent(FMLCommonSetupEvent event)
    {
        new CommonNetworkMod(handler);
    }
}
