package dev.cammiescorner.devotion.common.registry;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.blocks.AmethystAltarBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class DevotionBlocks {
	//-----Block Map-----//
	public static final LinkedHashMap<Block, Identifier> BLOCKS = new LinkedHashMap<>();

	//-----Blocks-----//
	public static final Block AMETHYST_ALTAR = create("amethyst_altar", new AmethystAltarBlock());

	//-----Registry-----//
	public static void register() {
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.ITEM, BLOCKS.get(block), getItem(block)));
	}

	private static BlockItem getItem(Block block) {
		return new BlockItem(block, new Item.Settings().group(Devotion.ITEM_GROUP));
	}

	private static <T extends Block> T create(String name, T block) {
		BLOCKS.put(block, Devotion.id(name));
		return block;
	}
}
