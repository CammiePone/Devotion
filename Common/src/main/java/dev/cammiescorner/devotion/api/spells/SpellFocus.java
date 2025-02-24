package dev.cammiescorner.devotion.api.spells;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.common.registries.DevotionSpellFoci;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;

public class SpellFocus {
	public static final Codec<Holder<SpellFocus>> CODEC = RegistryFixedCodec.create(DevotionRegistries.SPELL_FOCUS);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SpellFocus>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.SPELL_FOCUS);
	private ResourceLocation resourceLocation;
	private String descriptionId;

	public ResourceLocation getResourceLocation() {
		if(resourceLocation == null)
			resourceLocation = DevotionSpellFoci.REGISTRY.getKey(this);

		return resourceLocation;
	}

	public ResourceLocation getItemModelLocation() {
		return getResourceLocation().withPrefix("item/devotion/spell_focus/");
	}

	public ResourceLocation getStaffModelLocation() {
		return getResourceLocation().withPrefix("devotion/spell_focus/");
	}

	public String getDescriptionId() {
		if(descriptionId == null)
			descriptionId = Util.makeDescriptionId("item", DevotionSpellFoci.REGISTRY.getKey(this));

		return descriptionId;
	}
}
