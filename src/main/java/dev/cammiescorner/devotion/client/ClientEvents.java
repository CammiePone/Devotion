package dev.cammiescorner.devotion.client;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.api.events.client.KeyBindingCallback;
import dev.cammiescorner.devotion.client.screens.ResearchScreen;
import dev.cammiescorner.devotion.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.devotion.common.registry.DevotionKeyBinds;
import dev.cammiescorner.devotion.common.registry.DevotionScreenHandlers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public class ClientEvents {
	public static final Identifier HUD_ELEMENTS = Devotion.id("textures/gui/hud/hud_elements.png");

	@Environment(EnvType.CLIENT)
	public static void events() {
		HandledScreens.register(DevotionScreenHandlers.RESEARCH_SCREEN_HANDLER, ResearchScreen::new);

		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();

			if(client.player != null) {
				PlayerEntity player = client.player;

				if(DevotionHelper.isCasting(player) && DevotionKeyBinds.spellInvKey.isPressed()) {
					client.mouse.unlockCursor();
				}
			}
		});

		ClientTickEvents.START.register(client -> {
			if(client.player != null) {
				SetCastingPacket.send(DevotionKeyBinds.castingMode.isPressed());
			}
		});

		ClientTickEvents.END.register(client -> DevotionClient.clientTick++);

		KeyBindingCallback.UNPRESSED.register((key, modifiers) -> {
			MinecraftClient client = MinecraftClient.getInstance();

			if(client.player != null) {
				PlayerEntity player = client.player;

				if(DevotionHelper.isCasting(player) && DevotionKeyBinds.spellInvKey.matchesKey(key.getKeyCode(), key.getKeyCode()))
					client.mouse.lockCursor();
			}
		});
	}
}
