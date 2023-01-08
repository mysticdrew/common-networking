package commonnetwork.networking.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public record PacketContainer<T>(ResourceLocation packetIdentifier,
                                 Class<T> messageType,
                                 BiConsumer<T, FriendlyByteBuf> encoder,
                                 Function<FriendlyByteBuf, T> decoder,
                                 Consumer<PacketContext<T>> handler)
{
}
