package dev.cammiescorner.devotion.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.cammiescorner.devotion.api.DevotionHelper;
import net.minecraft.network.PacketByteBuf;

public class RestoreAuraAltarAction extends AltarAction {
	@Override
	public ConfiguredAltarAction create(JsonObject json) throws JsonParseException {
		return ConfiguredAltarAction.of((world, player, altar) -> DevotionHelper.setAura(player, 20), buf -> {}, this);
	}

	@Override
	public ConfiguredAltarAction create(PacketByteBuf buf) {
		return ConfiguredAltarAction.ofClient((world, player, altar) -> DevotionHelper.setAura(player, 20), this);
	}

	@Override
	public boolean requiresPlayer() {
		return true;
	}
}
