package commonnetwork;


import commonnetwork.api.Dispatcher;
import commonnetwork.networking.ForgeNetworkHandler;
import commonnetwork.networking.data.Side;
import commonnetwork.test.ExamplePacketOne;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class CommonNetworkForge
{
    public CommonNetworkForge()
    {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);


    }

    public void commonSetupEvent(FMLCommonSetupEvent event)
    {
        new CommonNetworkMod(new ForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER));
        if (FMLLoader.getDist().isClient())
        {
            MinecraftForge.EVENT_BUS.addListener(this::onClientJoined);

        }
        else
        {
            MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedInEvent);

        }
    }


    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer player)
        {
            this.onPlayerJoinServer(player);
        }
    }

    public void onJoinWorld()
    {
        Dispatcher.sendToServer(new ExamplePacketOne("Forge Client Message to Server"), false);
    }

    @SubscribeEvent
    public void onClientJoined(ClientPlayerNetworkEvent.LoggingIn event)
    {
        this.onJoinWorld();
    }

    public void onPlayerJoinServer(ServerPlayer player)
    {
        Dispatcher.sendToClient(new ExamplePacketOne("Forge Server Message to Client"), player);
        // either works fine.
    }
}
