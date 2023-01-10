package commonnetwork.api;

import net.minecraft.server.level.ServerPlayer;

public class Dispatcher
{
    /**
     * Sends the packet to the server, if the server has the packet registered.
     *
     * @param packet - the packet
     * @param <T>    - The type
     */
    public static <T> void sendToServer(T packet)
    {
        Network.getNetworkHandler().sendToServer(packet);
    }

    /**
     * Sends the packet to the server. Can ignore the check if the server has the packet registered.
     * Likely use case for this is talking to bukkit/spigot/paper servers.
     *
     * @param packet      - the packet
     * @param ignoreCheck - ignore the check if the server has the packet registered.
     * @param <T>         - The type
     */
    public static <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToServer(packet, ignoreCheck);
    }

    /**
     * Sends the packet to the client player, only if the player has the packet registered.
     *
     * @param packet - the packet
     * @param player - the player
     * @param <T>    - The type
     */
    public static <T> void sendToClient(T packet, ServerPlayer player)
    {
        Network.getNetworkHandler().sendToClient(packet, player);
    }
}
