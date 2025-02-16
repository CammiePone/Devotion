package dev.cammiescorner.devotion.api.staves;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.ItemStack;

public record StaffCap(float discount, boolean inert, boolean hasGlint) {
	public static final Codec<Holder<StaffCap>> CODEC = RegistryFixedCodec.create(DevotionRegistries.STAFF_CAP);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<StaffCap>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.STAFF_CAP);

	public StaffCap(float discount) {
		this(discount, false, false);
	}

	public StaffCap(float discount, boolean inert) {
		this(discount, inert, !inert);
	}

	public static float discount(ItemStack stack) {
		return stack.get(DevotionData.STAFF_CAP.get()).value().discount();
	}
}
