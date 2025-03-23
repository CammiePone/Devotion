package dev.cammiescorner.devotion.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.devotion.client.gui.screens.ScriptsOfDevotionScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class ArrowWidget extends AbstractButton {
	private final OnPress onPress;
	public final boolean pointsLeft;

	public ArrowWidget(int x, int y, boolean pointsLeft, OnPress onPress) {
		super(x, y, 24, 8, Component.empty());
		this.pointsLeft = pointsLeft;
		this.onPress = onPress;
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		if(active) {
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
			int u = pointsLeft ? 384 : 408;
			int v = isHovered() ? 32 : 24;

			guiGraphics.blit(ScriptsOfDevotionScreen.TEXTURE, getX(), getY(), u, v, width, height, 512, 512);
		}
	}

	@Override
	public void onPress() {
		onPress.onPress(this);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

	}

	public interface OnPress {
		void onPress(ArrowWidget widget);
	}
}
