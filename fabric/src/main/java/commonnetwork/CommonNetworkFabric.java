package commonnetwork;

import commonnetwork.api.Dispatcher;
import commonnetwork.networking.FabricNetworkHandler;
import commonnetwork.networking.data.Side;
import commonnetwork.test.ExamplePacketOne;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class CommonNetworkFabric implements ModInitializer, ClientModInitializer
{

    public CommonNetworkFabric()
    {
    }

    @Override
    public void onInitialize()
    {
        new CommonNetworkMod(new FabricNetworkHandler(Side.SERVER));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, client) -> this.onJoinWorld2(handler));
    }

    @Override
    public void onInitializeClient()
    {
        new CommonNetworkMod(new FabricNetworkHandler(Side.CLIENT));
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> this.onJoinWorld());
    }

    public void onJoinWorld2(ServerGamePacketListenerImpl sender) {
        Dispatcher.sendToClient(new ExamplePacketOne("Fabric Message to Client"), sender.player);
        // either works fine.
//        Network.getNetworkHandler().sendToServer(new ExamplePacketOne());
    }

    public void onJoinWorld() {
        Dispatcher.sendToServer(new ExamplePacketOne("Fabric Message to Server"));
        // either works fine.
//        Network.getNetworkHandler().sendToServer(new ExamplePacketOne());
    }
}
