package dev.cammiescorner.devotion.common.registry;

import com.mojang.blaze3d.platform.InputUtil;
import dev.cammiescorner.devotion.Devotion;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import net.minecraft.client.option.StickyKeyBind;
import org.lwjgl.glfw.GLFW;

public class DevotionKeyBinds {
	public static final String DEVOTION_CATEGORY = "category." + Devotion.MOD_ID + ".keys";
	public static KeyBind castingMode;
	public static KeyBind spellInvKey;

	public static void register() {
		castingMode = KeyBindingHelper.registerKeyBinding(new StickyKeyBind(
				"key." + Devotion.MOD_ID + ".swapCastingMode",
				GLFW.GLFW_KEY_X,
				DevotionKeyBinds.DEVOTION_CATEGORY,
				() -> true
		));

		spellInvKey = KeyBindingHelper.registerKeyBinding(new KeyBind(
				"key." + Devotion.MOD_ID + ".openSpellInv",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_ALT,
				DevotionKeyBinds.DEVOTION_CATEGORY
		));
	}
}
