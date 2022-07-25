package dev.cammiescorner.devotion.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.api.events.client.KeyBindingCallback;
import dev.cammiescorner.devotion.client.screens.GuideBookScreen;
import dev.cammiescorner.devotion.client.screens.ResearchScreen;
import dev.cammiescorner.devotion.common.packets.c2s.ChangeSpellPacket;
import dev.cammiescorner.devotion.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.devotion.common.registry.DevotionKeyBinds;
import dev.cammiescorner.devotion.common.registry.DevotionScreenHandlers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public class ClientEvents {
	public static final Identifier HUD_ELEMENTS = Devotion.id("textures/gui/hud/hud_elements.png");
	private static int hoveredSpellIndex = 10;

	@Environment(EnvType.CLIENT)
	public static void events() {
		HandledScreens.register(DevotionScreenHandlers.RESEARCH_SCREEN_HANDLER, ResearchScreen::new);
		HandledScreens.register(DevotionScreenHandlers.GUIDE_BOOK_SCREEN_HANDLER, GuideBookScreen::new);

		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();

			if(client.player != null) {
				RenderSystem.setShaderTexture(0, ClientEvents.HUD_ELEMENTS);
				PlayerEntity player = client.player;

				if(DevotionHelper.isCasting(player) && DevotionKeyBinds.spellInvKey.isPressed()) {
					int spellCount = DevotionHelper.getMaxSpells(player);
					int x = (int) ((client.getWindow().getScaledWidth() - 128) * 0.5F);
					int y = (int) ((client.getWindow().getScaledHeight() - 128) * 0.5F);
					float mouseX = (float) client.mouse.getX() * (client.getWindow().getScaledWidth() / (float) client.getWindow().getWidth());
					float mouseY = (float) client.mouse.getY() * (client.getWindow().getScaledHeight() / (float) client.getWindow().getHeight());
					float angleBetween = (float) Math.toRadians(360D / spellCount);
					Vec2f mousePos = new Vec2f(mouseX, mouseY);

					client.mouse.unlockCursor();
					DrawableHelper.drawTexture(matrices, x, y, 0, 0, 128, 128, 256, 256);

					matrices.push();
					matrices.translate(x + 52, y + 52, 0);

					if(mousePos.distanceSquared(new Vec2f(x + 64, y + 64)) <= 225) {
						matrices.translate(12, 12, 0);
						matrices.scale(1.25F, 1.25F, 1);
						matrices.translate(-12, -12, 0);
						hoveredSpellIndex = 10;
					}

					DrawableHelper.drawTexture(matrices, 0, 0, 0, 128, 24, 24, 256, 256);
					matrices.pop();

					for(int i = 0; i < spellCount; i++) {
						int radialX = radialPosX(angleBetween, x + 64, i);
						int radialY = radialPosY(angleBetween, y + 64, i);
						Vec2f radialElementPos = new Vec2f(radialX, radialY);

						matrices.push();
						matrices.translate(radialX - 12, radialY - 12, 0);

						if(mousePos.distanceSquared(radialElementPos) <= 225) {
							matrices.translate(12, 12, 0);
							matrices.scale(1.25F, 1.25F, 1);
							matrices.translate(-12, -12, 0);
							hoveredSpellIndex = i;
						}

						DrawableHelper.drawTexture(matrices, 0, 0, 0, 128, 24, 24, 256, 256);
						matrices.pop();
					}

					if(DevotionHelper.getSelectedSpellIndex(player) != hoveredSpellIndex)
						ChangeSpellPacket.send(hoveredSpellIndex);
				}
			}
		});

		ClientTickEvents.START.register(client -> {
			if(client.player != null)
				SetCastingPacket.send(DevotionKeyBinds.castingMode.isPressed());
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

	private static int radialPosX(double angle, int x, int index) {
		return x + (int) (Math.cos(angle * index - (Math.PI * 0.5)) * 58);
	}

	private static int radialPosY(double angle, int y, int index) {
		return y + (int) (Math.sin(angle * index - (Math.PI * 0.5)) * 58);
	}
}
