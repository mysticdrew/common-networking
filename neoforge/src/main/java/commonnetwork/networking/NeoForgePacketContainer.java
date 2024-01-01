package commonnetwork.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.function.BiConsumer;

public record NeoForgePacketContainer<T>(Class<T> messageType,
                                         ResourceLocation packetIdentifier,
                                         BiConsumer<T, FriendlyByteBuf> encoder,
                                         FriendlyByteBuf.Reader<NeoForgePacket<T>> decoder,
                                         IPayloadHandler<NeoForgePacket<T>> handler)
{
}
