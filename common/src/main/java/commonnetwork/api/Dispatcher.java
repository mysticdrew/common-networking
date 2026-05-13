package commonnetwork.api;

import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Static convenience facade over {@link NetworkHandler}. Every method delegates to {@link Network#getNetworkHandler()};
 * use this when you don't want to thread the handler reference through your code.
 */
public class Dispatcher
{
    /**
     * Sends the packet to the server, if the server has the packet registered.
     *
     * @param packet - the packet
     * @param <T>    - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToServer(T packet)
    {
        Network.getNetworkHandler().sendToServer(packet);
    }

    /**
     * Sends the packet to the server. Can ignore the check if the server has the packet registered.
     * Likely use case for this is talking to bukkit/spigot/paper servers.
     *
     * @param packet      - the packet
     * @param ignoreCheck - ignore the check if the server has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToServer(T packet, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToServer(packet, ignoreCheck);
    }

    /**
     * Sends the packet to the connection.
     *
     * @param packet     - the packet
     * @param connection - the connection
     * @param <T>        - The packet type
     */
    public static <T extends CustomPacketPayload> void send(T packet, Connection connection)
    {
        Network.getNetworkHandler().send(packet, connection);
    }

    /**
     * Sends the packet to the client player, only if the player has the packet registered.
     *
     * @param packet - the packet
     * @param player - the player
     * @param <T>    - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClient(T packet, ServerPlayer player)
    {
        Network.getNetworkHandler().sendToClient(packet, player);
    }

    /**
     * Sends the packet to the client player.
     *
     * @param packet      - the packet
     * @param player      - the player
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToClient(packet, player, ignoreCheck);
    }

    /**
     * Sends the packet to the client players, only if the players has the packet registered.
     *
     * @param packet  - the packet
     * @param players - the players
     * @param <T>     - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClients(T packet, List<ServerPlayer> players)
    {
        Network.getNetworkHandler().sendToClients(packet, players);
    }

    /**
     * Sends the packet to the client players.
     *
     * @param packet      - the packet
     * @param players     - the players
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClients(T packet, List<ServerPlayer> players, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToClients(packet, players, ignoreCheck);
    }

    /**
     * Sends the packet to all the client players in the server, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param server - the server
     * @param <T>    - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToAllClients(T packet, MinecraftServer server)
    {
        Network.getNetworkHandler().sendToAllClients(packet, server);
    }

    /**
     * Sends the packet to all the client players in the server.
     *
     * @param packet      - the packet
     * @param server      - the server
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToAllClients(T packet, MinecraftServer server, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToAllClients(packet, server, ignoreCheck);
    }

    /**
     * Sends the packet to all the client players in the level, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param level  - the level
     * @param <T>    - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsInLevel(T packet, ServerLevel level)
    {
        Network.getNetworkHandler().sendToClientsInLevel(packet, level);
    }

    /**
     * Sends the packet to all the client players in the level.
     *
     * @param packet      - the packet
     * @param level       - the level
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsInLevel(T packet, ServerLevel level, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToClientsInLevel(packet, level, ignoreCheck);
    }

    /**
     * Sends the packet to all the client players loading a chunk, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param chunk  - the chunk
     * @param <T>    - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsLoadingChunk(T packet, LevelChunk chunk)
    {
        Network.getNetworkHandler().sendToClientsLoadingChunk(packet, chunk);
    }

    /**
     * Sends the packet to all the client players loading a chunk.
     *
     * @param packet      - the packet
     * @param chunk       - the chunk
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsLoadingChunk(T packet, LevelChunk chunk, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToClientsLoadingChunk(packet, chunk, ignoreCheck);
    }

    /**
     * Sends the packet to all the client players loading a position, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param level  - the level
     * @param pos    - the chunkpos
     * @param <T>    - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsLoadingPos(T packet, ServerLevel level, ChunkPos pos)
    {
        Network.getNetworkHandler().sendToClientsLoadingPos(packet, level, pos);
    }

    /**
     * Sends the packet to all the client players loading a position.
     *
     * @param packet      - the packet
     * @param level       - the level
     * @param pos         - the chunkpos
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsLoadingPos(T packet, ServerLevel level, ChunkPos pos, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToClientsLoadingPos(packet, level, pos, ignoreCheck);
    }

    /**
     * Sends the packet to all the client players loading a position, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param level  - the level
     * @param pos    - the blockpos
     * @param <T>    - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsLoadingPos(T packet, ServerLevel level, BlockPos pos)
    {
        Network.getNetworkHandler().sendToClientsLoadingPos(packet, level, pos);
    }

    /**
     * Sends the packet to all the client players loading a position.
     *
     * @param packet      - the packet
     * @param level       - the level
     * @param pos         - the blockpos
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsLoadingPos(T packet, ServerLevel level, BlockPos pos, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToClientsLoadingPos(packet, level, pos, ignoreCheck);
    }

    /**
     * Sends the packet to all the client players in range of a position, only if the players has the packet registered.
     *
     * @param packet - the packet
     * @param level  - the level
     * @param pos    - the blockpos
     * @param range  - the range
     * @param <T>    - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsInRange(T packet, ServerLevel level, BlockPos pos, double range)
    {
        Network.getNetworkHandler().sendToClientsInRange(packet, level, pos, range);
    }

    /**
     * Sends the packet to all the client players in range of a position.
     *
     * @param packet      - the packet
     * @param level       - the level
     * @param pos         - the blockpos
     * @param range       - the range
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>         - The packet type
     */
    public static <T extends CustomPacketPayload> void sendToClientsInRange(T packet, ServerLevel level, BlockPos pos, double range, boolean ignoreCheck)
    {
        Network.getNetworkHandler().sendToClientsInRange(packet, level, pos, range, ignoreCheck);
    }

    /**
     * Generates a {@link ClientboundCustomPayloadPacket} wrapping the given packet without sending it.
     *
     * @param packet - the packet
     * @param <T>    - The packet type
     * @return The packet wrapped into a {@link ClientboundCustomPayloadPacket}, or null if the packet is not registered.
     */
    public static <T extends CustomPacketPayload> @Nullable ClientboundCustomPayloadPacket getRawClientboundPacket(T packet)
    {
        return Network.getNetworkHandler().getRawClientboundPacket(packet);
    }

    /**
     * Generates a {@link ServerboundCustomPayloadPacket} wrapping the given packet without sending it.
     *
     * @param packet - the packet
     * @param <T>    - The packet type
     * @return The packet wrapped into a {@link ServerboundCustomPayloadPacket}, or null if the packet is not registered.
     */
    public static <T extends CustomPacketPayload> @Nullable ServerboundCustomPayloadPacket getRawServerboundPacket(T packet)
    {
        return Network.getNetworkHandler().getRawServerboundPacket(packet);
    }
}
