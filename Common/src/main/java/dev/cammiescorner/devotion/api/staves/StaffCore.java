package dev.cammiescorner.devotion.api.staves;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

public class StaffCore {
	public static final Codec<Holder<StaffCore>> CODEC = RegistryFixedCodec.create(DevotionRegistries.STAFF_CORE);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<StaffCore>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.STAFF_CORE);
	private String descriptionId;

	public String getDescriptionId() {
		if(descriptionId == null)
			descriptionId = Util.makeDescriptionId("staff_core", DevotionStaffCores.REGISTRY.getKey(this));

		return descriptionId;
	}
}
