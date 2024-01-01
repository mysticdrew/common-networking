package commonnetwork.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NeoForgePacket<T>(NeoForgePacketContainer<T> container, T packet) implements CustomPacketPayload
{


    @Override
    public void write(FriendlyByteBuf buff)
    {
        container().encoder().accept(packet(), buff);
    }

    @Override
    public ResourceLocation id()
    {
        return container().packetIdentifier();
    }
}
