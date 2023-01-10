package example.forge;

import example.client.ExampleModCommonClient;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ExampleModForge
{
    ExampleModCommon commonMod;
    ExampleModCommonClient modCommonClient;

    public ExampleModForge()
    {
        commonMod = new ExampleModCommon();
        modCommonClient = new ExampleModCommonClient();
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedInEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onClientJoined);
    }

    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer player)
        {
            commonMod.onPlayerJoinServer(player);
        }
    }

    @SubscribeEvent
    public void onClientJoined(ClientPlayerNetworkEvent.LoggingIn event)
    {
        modCommonClient.onJoinWorld();
    }
}
