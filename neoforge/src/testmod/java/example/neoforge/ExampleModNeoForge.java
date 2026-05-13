package example.neoforge;

import example.Constants;
import example.ExampleModCommon;
import example.client.ExampleModCommonClient;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(Constants.MOD_ID)
public class ExampleModNeoForge
{
    private final ExampleModCommon commonMod;
    private final ExampleModCommonClient clientMod;

    public ExampleModNeoForge(IEventBus eventBus)
    {
        commonMod = new ExampleModCommon();
        clientMod = new ExampleModCommonClient();
        NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedInEvent);
        NeoForge.EVENT_BUS.addListener(this::onClientJoined);
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
        clientMod.onJoinWorld();
    }
}
