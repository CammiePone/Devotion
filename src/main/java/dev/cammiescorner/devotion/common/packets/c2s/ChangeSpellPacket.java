package dev.cammiescorner.devotion.common.packets.c2s;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.DevotionHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class ChangeSpellPacket {
	public static final Identifier ID = Devotion.id("change_spell");

	public static void send(int spellIndex) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(spellIndex);

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		int spellIndex = buf.readVarInt();

		server.execute(() -> {
			// sets spell to Empty
			if(spellIndex >= 10) {
				DevotionHelper.setSelectedSpell(player, 10);
				return;
			}

			DevotionHelper.setSelectedSpell(player, spellIndex);
		});
	}
}
