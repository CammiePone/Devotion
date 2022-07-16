package dev.cammiescorner.devotion.common.registry;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class DevotionTags {
	public static final TagKey<Block> ALTAR_PALETTE = TagKey.of(Registry.BLOCK_KEY, Devotion.id("altar_palette"));
}
