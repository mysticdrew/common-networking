package commonnetwork.api;

import net.minecraft.server.level.ServerPlayer;

public class Dispatcher
{
    /**
     * Sends the packet to the server, if the server has the packet registered.
     *
     * @param packet - the packet
     */
    public static <T> void sendToServer(T packet)
    {
        Network.getDispatcher().sendToServer(packet);
    }

    /**
     * Sends the packet to the server. Can ignore the check if the server has the packet registered.
     * Likely use case for this is talking to bukkit/spigot/paper servers.
     *
     * @param packet      - the packet
     * @param ignoreCheck - ignore the check if the server has the packet registered.
     */
    public static <T> void sendToServer(T packet, boolean ignoreCheck)
    {
        Network.getDispatcher().sendToServer(packet, ignoreCheck);
    }

    /**
     * Sends the packet to the client player, only if the player has the packet registered.
     *
     * @param packet - the packet
     * @param player - the player
     */
    public static <T> void sendToClient(T packet, ServerPlayer player)
    {
        Network.getDispatcher().sendToClient(packet, player);
    }

    /**
     * Sends the packet to the client player. Can ignore the check if the client has the id registered.
     *
     * @param packet      - the packet
     * @param player      - the player
     * @param ignoreCheck - ignore the check if the player has the packet registered.
     */
    public static <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        Network.getDispatcher().sendToClient(packet, player, ignoreCheck);
    }
}
