package dev.cammiescorner.devotion.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.api.spells.AuraType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.JsonHelper;

import java.util.Locale;

public class SetAffinityAltarAction extends AltarAction {
	@Override
	public ConfiguredAltarAction create(JsonObject json) throws JsonParseException {
		String affinityId = JsonHelper.getString(json, "affinity", "none").toUpperCase(Locale.ROOT);
		AuraType affinity = AuraType.valueOf(affinityId);

		return ConfiguredAltarAction.of((world, player, altar) -> DevotionHelper.setAffinity(player, affinity), buf -> buf.writeString(affinityId), this);
	}

	@Override
	public ConfiguredAltarAction create(PacketByteBuf buf) {
		String affinityId = buf.readString().toUpperCase(Locale.ROOT);
		AuraType affinity = AuraType.valueOf(affinityId);

		return ConfiguredAltarAction.ofClient((world, player, altar) -> DevotionHelper.setAffinity(player, affinity), this);
	}

	@Override
	public boolean requiresPlayer() {
		return true;
	}
}
