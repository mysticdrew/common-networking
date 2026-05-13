package example.forge;

import example.Constants;
import example.ExampleModCommon;
import example.client.ExampleModCommonClient;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.lang.invoke.MethodHandles;

@Mod(Constants.MOD_ID)
public class ExampleModForge
{
    private final ExampleModCommon commonMod;
    private final ExampleModCommonClient clientMod;

    public ExampleModForge(FMLJavaModLoadingContext context)
    {
        commonMod = new ExampleModCommon();
        clientMod = FMLEnvironment.dist.isClient() ? new ExampleModCommonClient() : null;
        BusGroup.DEFAULT.register(MethodHandles.lookup(), this);
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
        if (clientMod != null)
        {
            clientMod.onJoinWorld();
        }
    }
}
