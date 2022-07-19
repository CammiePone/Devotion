package dev.cammiescorner.devotion.common.packets.s2c;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.screens.ResearchScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.Collection;

public class RefreshResearchScreenPacket {
	public static final Identifier ID = Devotion.id("refresh_research_screen");

	public static void send(Collection<ServerPlayerEntity> players, ItemStack stack) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeItemStack(stack);

		ServerPlayNetworking.send(players, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		ItemStack stack = buf.readItemStack();

		client.execute(() -> {
			if(client.currentScreen instanceof ResearchScreen screen) {
				screen.getScreenHandler().setScroll(stack);
				screen.redrawLines();
			}
		});
	}
}
