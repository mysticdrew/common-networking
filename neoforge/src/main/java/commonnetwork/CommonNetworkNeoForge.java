package commonnetwork;


import commonnetwork.api.Dispatcher;
import commonnetwork.api.Network;
import commonnetwork.networking.NeoForgeNetworkHandler;
import commonnetwork.networking.PacketRegistrationHandler;
import commonnetwork.networking.data.Side;
import commonnetwork.test.ExamplePacketOne;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;


@Mod(Constants.MOD_ID)
public class CommonNetworkNeoForge
{
    private final PacketRegistrationHandler handler;

    public CommonNetworkNeoForge()
    {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);
        handler = new NeoForgeNetworkHandler(FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER);
        FMLJavaModLoadingContext.get().getModEventBus().register(handler);
        NeoForge.EVENT_BUS.addListener(this::onPlayerJoinServer);
        NeoForge.EVENT_BUS.addListener(this::onJoinWorldEvent);
        Network
                .registerPacket(ExamplePacketOne.CHANNEL, ExamplePacketOne.class, ExamplePacketOne::encode, ExamplePacketOne::decode, ExamplePacketOne::handle);

    }

    public void commonSetupEvent(FMLCommonSetupEvent event)
    {
        new CommonNetworkMod(handler);
    }

    public void onJoinWorldEvent(ClientPlayerNetworkEvent.LoggingIn event)
    {
        Dispatcher.sendToServer(new ExamplePacketOne("NeoForge Message to Server"), false);
    }
    public void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        Dispatcher.sendToClient(new ExamplePacketOne("NeoForge Message to Client"), player);
    }
}
