package dev.cammiescorner.devotion.api.wands;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

public class WandCap {
	public static final Codec<Holder<WandCap>> CODEC = RegistryFixedCodec.create(DevotionRegistryKeys.WAND_CAP);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WandCap>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistryKeys.WAND_CAP);
}
