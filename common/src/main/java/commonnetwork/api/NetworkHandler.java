package commonnetwork.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;

public interface NetworkHandler
{
    /**
     * Sends the packet to the server, if the server has the packet registered.
     *
     * @param packet - the packet
     * @param <T>    - The packet
     */
    default <T> void sendToServer(T packet)
    {
        sendToServer(packet, false);
    }

    /**
     * Sends the packet to the server. Can ignore the check if the server has the packet registered.
     * Likely use case for this is talking to bukkit/spigot/paper servers.
     *
     * @param packet      - the packet
     * @param ignoreCheck - ignore the check if the server has the packet registered.
     * @param <T>         - The packet
     */
    <T> void sendToServer(T packet, boolean ignoreCheck);

    /**
     * Sends the packet to the client player, only if the player has the packet registered.
     *
     * @param packet - the packet
     * @param player - the player
     * @param <T>    - The packet
     */
    default <T> void sendToClient(T packet, ServerPlayer player)
    {
        sendToClient(packet, player, false);
    }

    /**
     * Sends the packet to the client player..
     *
     * @param packet      - the packet
     * @param player      - the player
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet
     */
    <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck);

    /**
     * Sends the packet to the client players, only if the players has the packet registered.
     *
     * @param packet  - the packet
     * @param players - the players
     * @param <T>     - The packet
     */
    default <T> void sendToClients(T packet, List<ServerPlayer> players)
    {
        sendToClients(packet, players, false);
    }

    /**
     * Sends the packet to the client players.
     *
     * @param packet      - the packet
     * @param players     - the players
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet
     */
    default <T> void sendToClients(T packet, List<ServerPlayer> players, boolean ignoreCheck)
    {
        for (ServerPlayer player : players)
        {
            sendToClient(packet, player, ignoreCheck);
        }
    }

    /**
     * Sends the packet to all the client players in the server, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param server - the server
     * @param <T>    - The packet
     */
    default <T> void sendToAllClients(T packet, MinecraftServer server)
    {
        sendToAllClients(packet, server, false);
    }

    /**
     * Sends the packet to all the client players in the server
     *
     * @param packet      - the packet
     * @param server      - the server
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet
     */
    default <T> void sendToAllClients(T packet, MinecraftServer server, boolean ignoreCheck)
    {
        sendToClients(packet, server.getPlayerList().getPlayers(), ignoreCheck);
    }

    /**
     * Sends the packet to all the client players in the level, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param level  - the level
     * @param <T>    - The packet
     */
    default <T> void sendToClientsInLevel(T packet, ServerLevel level)
    {
        sendToClientsInLevel(packet, level, false);
    }

    /**
     * Sends the packet to all the client players in the level.
     *
     * @param packet      - the packet
     * @param level       - the level
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet
     */
    default <T> void sendToClientsInLevel(T packet, ServerLevel level, boolean ignoreCheck)

    {
        sendToClients(packet, level.players(), ignoreCheck);
    }

    /**
     * Sends the packet to all the client players loading a chunk, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param chunk  - the chunk
     * @param <T>    - The packet
     */
    default <T> void sendToClientsLoadingChunk(T packet, LevelChunk chunk)
    {
        sendToClientsLoadingChunk(packet, chunk, false);
    }

    /**
     * Sends the packet to all the client players loading a chunk.
     *
     * @param packet      - the packet
     * @param chunk       - the chunk
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet
     */
    default <T> void sendToClientsLoadingChunk(T packet, LevelChunk chunk, boolean ignoreCheck)
    {
        ServerChunkCache chunkCache = (ServerChunkCache) chunk.getLevel().getChunkSource();
        sendToClients(packet, chunkCache.chunkMap.getPlayers(chunk.getPos(), false), ignoreCheck);
    }

    /**
     * Sends the packet to all the client players loading a position, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param level  - the level
     * @param pos    - the chunkpos
     * @param <T>    - The packet
     */
    default <T> void sendToClientsLoadingPos(T packet, ServerLevel level, ChunkPos pos)
    {
        sendToClientsLoadingPos(packet, level, pos, false);
    }

    /**
     * Sends the packet to all the client players loading a position.
     *
     * @param packet      - the packet
     * @param level       - the level
     * @param pos         - the chunkpos
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet
     */
    default <T> void sendToClientsLoadingPos(T packet, ServerLevel level, ChunkPos pos, boolean ignoreCheck)
    {
        sendToClientsLoadingChunk(packet, level.getChunk(pos.x, pos.z), ignoreCheck);
    }

    /**
     * Sends the packet to all the client players loading a position, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param level  - the level
     * @param pos    - the blockpos
     * @param <T>    - The packet
     */
    default <T> void sendToClientsLoadingPos(T packet, ServerLevel level, BlockPos pos)
    {
        sendToClientsLoadingPos(packet, level, pos, false);
    }

    /**
     * Sends the packet to all the client players loading a position
     *
     * @param packet      - the packet
     * @param level       - the level
     * @param pos         - the blockpos
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet
     */
    default <T> void sendToClientsLoadingPos(T packet, ServerLevel level, BlockPos pos, boolean ignoreCheck)
    {
        sendToClientsLoadingPos(packet, level, new ChunkPos(pos), ignoreCheck);
    }

    /**
     * Sends the packet to all the client players in range of a position, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param level  - the level
     * @param pos    - the blockpos
     * @param range  - the range
     * @param <T>    - The packet
     */
    default <T> void sendToClientsInRange(T packet, ServerLevel level, BlockPos pos, double range)
    {
        sendToClientsInRange(packet, level, pos, range, false);
    }

    /**
     * Sends the packet to all the client players in range of a position.
     *
     * @param packet      - the packet
     * @param level       - the level
     * @param pos         - the blockpos
     * @param range       - the range
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet
     */
    default <T> void sendToClientsInRange(T packet, ServerLevel level, BlockPos pos, double range, boolean ignoreCheck)
    {
        for (ServerPlayer player : level.players())
        {
            if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= range * range)
            {
                sendToClient(packet, player, ignoreCheck);
            }
        }
    }
}
