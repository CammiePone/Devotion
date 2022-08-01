package dev.cammiescorner.devotion.common.registry;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.screens.ResearchScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class DevotionScreenHandlers {
	//-----Screen Handler Map-----//
	public static final LinkedHashMap<ScreenHandlerType<?>, Identifier> SCREEN_HANDLERS = new LinkedHashMap<>();

	//-----Screen Handlers-----//
	public static final ScreenHandlerType<ResearchScreenHandler> RESEARCH_SCREEN_HANDLER = create("research_screen_handler", new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new ResearchScreenHandler(syncId, inventory, buf.readItemStack())));

	//-----Registry-----//
	public static void register() {
		SCREEN_HANDLERS.keySet().forEach(type -> Registry.register(Registry.SCREEN_HANDLER, SCREEN_HANDLERS.get(type), type));
	}

	private static <T extends ScreenHandlerType<?>> T create(String name, T type) {
		SCREEN_HANDLERS.put(type, Devotion.id(name));
		return type;
	}
}
