package commonnetwork.api;

import net.minecraft.server.level.ServerPlayer;

public interface NetworkHandler
{
    /**
     * Sends the packet to the server, if the server has the packet registered.
     *
     * @param packet - the packet
     * @param <T>    - The type
     */
    <T> void sendToServer(T packet);

    /**
     * Sends the packet to the server. Can ignore the check if the server has the packet registered.
     * Likely use case for this is talking to bukkit/spigot/paper servers.
     *
     * @param packet      - the packet
     * @param ignoreCheck - ignore the check if the server has the packet registered.
     * @param <T>         - The type
     */
    <T> void sendToServer(T packet, boolean ignoreCheck);

    /**
     * Sends the packet to the client player, only if the player has the packet registered.
     *
     * @param packet - the packet
     * @param player - the player
     * @param <T>    - The type
     */
    <T> void sendToClient(T packet, ServerPlayer player);
}
