import commonnetwork.Constants;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(Constants.MOD_ID)
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ExampleModForge
{
    ExampleModCommon commonMod;
    ExampleModCommonClient modCommonClient;

    public ExampleModForge(IEventBus eventBus)
    {
        commonMod = new ExampleModCommon();
        modCommonClient = new ExampleModCommonClient();
        eventBus.addListener(this::onPlayerLoggedInEvent);
        eventBus.addListener(this::onClientJoined);
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
