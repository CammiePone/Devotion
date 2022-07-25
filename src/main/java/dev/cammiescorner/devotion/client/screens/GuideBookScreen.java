package dev.cammiescorner.devotion.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.common.screens.GuideBookScreenHandler;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class GuideBookScreen extends HandledScreen<GuideBookScreenHandler> {
	public static final Identifier TEXTURE = Devotion.id("textures/gui/scripts_of_devotion_frame.png");
	public double offsetX, offsetY;

	public GuideBookScreen(GuideBookScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, Text.empty());
	}

	@Override
	protected void init() {
		super.init();
		x = (width - 378) / 2;
		y = (height - 250) / 2;
		offsetX = DevotionClient.guideBookOffsetX;
		offsetY = DevotionClient.guideBookOffsetY;
	}

	@Override
	public void closeScreen() {
		DevotionClient.guideBookOffsetX = offsetX;
		DevotionClient.guideBookOffsetY = offsetY;
		super.closeScreen();
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		DrawableHelper.drawTexture(matrices, x, y, 0, 256, 378, 250, 512, 512);
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		if(client != null) {
			// TODO draw research tree
			int scale = (int) client.getWindow().getScaleFactor();
			RenderSystem.enableScissor((x + 16) * scale, (y + 16) * scale, 346 * scale, 218 * scale);
			matrices.push();
			matrices.translate(offsetX, offsetY, 0);
			DrawableHelper.drawTexture(matrices, 174, 110, 384, 0, 30, 30, 512, 512);
			matrices.pop();
			RenderSystem.disableScissor();
		}

		DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 378, 250, 512, 512);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if(button == 0) {
			offsetX = MathHelper.clamp(offsetX + deltaX, -190, 190);
			offsetY = MathHelper.clamp(offsetY + deltaY, -125, 125);
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
}
