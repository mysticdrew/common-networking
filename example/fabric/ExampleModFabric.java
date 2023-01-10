package example.fabric;

import example.client.ExampleModCommonClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ExampleModFabric implements ModInitializer, ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        var clientMod = new ExampleModCommonClient();
        // Send the packet when we join the world.
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> clientMod.onJoinWorld());
    }

    @Override
    public void onInitialize()
    {
        var commonMod = new ExampleModCommon();

        // Send a packet to the player when they join the server.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> commonMod.onPlayerJoinServer(handler.player));
    }
}
