package dev.cammiescorner.devotion.api.staves;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

public class StaffCap {
	public static final Codec<Holder<StaffCap>> CODEC = RegistryFixedCodec.create(DevotionRegistries.STAFF_CAP);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<StaffCap>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.STAFF_CAP);
}
