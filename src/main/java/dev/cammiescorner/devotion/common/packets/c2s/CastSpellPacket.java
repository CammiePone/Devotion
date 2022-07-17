package dev.cammiescorner.devotion.common.packets.c2s;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.api.spells.SpellComplexity;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class CastSpellPacket {
	public static final Identifier ID = Devotion.id("cast_spell");

	public static void send() {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		server.execute(() -> {
			Spell spell = DevotionHelper.getSelectedSpell(player);
			if(DevotionHelper.canCastSpell(player, spell)) {
				if(spell.getSpellComplexity() == SpellComplexity.UNIQUE)
					DevotionHelper.setUniqueSpellActive(player, spell, !DevotionHelper.isUniqueSpellActive(player, spell));

				DevotionHelper.castCurrentSpell(player);
			}
		});
	}
}
