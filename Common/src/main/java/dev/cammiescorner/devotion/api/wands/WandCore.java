package dev.cammiescorner.devotion.api.wands;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

public class WandCore {
	public static final Codec<Holder<WandCore>> CODEC = RegistryFixedCodec.create(DevotionRegistryKeys.WAND_CORE);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WandCore>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistryKeys.WAND_CORE);
}
