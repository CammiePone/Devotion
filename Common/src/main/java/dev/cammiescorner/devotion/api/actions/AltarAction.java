package dev.cammiescorner.devotion.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;

public abstract class AltarAction {
	public abstract ConfiguredAltarAction create(JsonObject json) throws JsonParseException;

	public abstract ConfiguredAltarAction create(RegistryFriendlyByteBuf buf);

	public abstract MapCodec<ConfiguredAltarAction> codec();
}
