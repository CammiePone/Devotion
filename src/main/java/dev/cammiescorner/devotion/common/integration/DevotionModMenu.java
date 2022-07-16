package dev.cammiescorner.devotion.common.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.devotion.Devotion;

public class DevotionModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> DevotionConfig.getScreen(parent, Devotion.MOD_ID);
	}
}
