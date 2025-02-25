package dev.cammiescorner.devotion.common.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.gui.screens.ResearchScreen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;



public record ClientboundRefreshResearchScreenPacket(ItemStack stack) implements CustomPacketPayload {
	public static final Type<ClientboundRefreshResearchScreenPacket> TYPE = new Type<>(Devotion.id("refresh_research_screen"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRefreshResearchScreenPacket> CODEC = StreamCodec.of((buffer, value) -> {
		ItemStack.STREAM_CODEC.encode(buffer, value.stack);
	}, buffer -> new ClientboundRefreshResearchScreenPacket(ItemStack.STREAM_CODEC.decode(buffer)));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ClientboundRefreshResearchScreenPacket> context) {
		if(client.screen instanceof ResearchScreen screen) {
			screen.setScroll(context.message().stack());
			screen.redrawLines();
		}
	}
}
