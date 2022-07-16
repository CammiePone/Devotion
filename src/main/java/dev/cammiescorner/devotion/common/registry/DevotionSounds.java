package dev.cammiescorner.devotion.common.registry;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class DevotionSounds {
	public static final SoundEvent BLOCK_AMETHYST_CRYSTAL_GROW = new SoundEvent(Devotion.id("block.amethyst_block.grow"));

	public static void register() {
		Registry.register(Registry.SOUND_EVENT, BLOCK_AMETHYST_CRYSTAL_GROW.getId(), BLOCK_AMETHYST_CRYSTAL_GROW);
	}
}
