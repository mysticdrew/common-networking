package example.fabric;

import example.ExampleModCommon;
import example.client.ExampleModCommonClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ExampleModFabric implements ModInitializer, ClientModInitializer
{
    private ExampleModCommon commonMod;
    private ExampleModCommonClient clientMod;

    @Override
    public void onInitialize()
    {
        System.out.println("INIT-S");
        commonMod = new ExampleModCommon();
        // Push a packet to a player when they join the server.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> commonMod.onPlayerJoinServer(handler.player));
    }

    @Override
    public void onInitializeClient()
    {
        System.out.println("INIT-C");
        clientMod = new ExampleModCommonClient();
        // Fire a packet to the server when the client finishes joining a world.
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> clientMod.onJoinWorld());
    }
}
