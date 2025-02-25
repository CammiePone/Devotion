package dev.cammiescorner.devotion.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.gui.screens.ScriptsOfDevotionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;



public class TabWidget extends AbstractButton {
	public static final ResourceLocation TEXTURE = Devotion.id("textures/gui/scripts_of_devotion_icons.png");
	private final boolean top;
	private final ResourceLocation tabId;
	private final Item item;
	private final OnPress onPress;
	private float yPos = 0;

	public TabWidget(int x, int y, boolean top, ResourceLocation tabId, Item item, OnPress onPress) {
		super(x, y, 24, 40, Component.empty());
		this.top = top;
		this.tabId = tabId;
		this.item = item;
		this.onPress = onPress;
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		PoseStack poseStack = guiGraphics.pose();
		int u = 0;

		if(Minecraft.getInstance().screen instanceof ScriptsOfDevotionScreen guideBookScreen) {
			if(!guideBookScreen.tabId.equals(tabId)) {
				u = 24;

				if(isHoveredOrFocused())
					yPos = Math.min(10, yPos + 1);
				else
					yPos = Math.max(0, yPos - 1);
			}
			else {
				yPos = 10;
			}
		}

		float lerp = Mth.lerp(yPos / 10f, 0f, 17f);
		poseStack.pushPose();

		if(top) {
			poseStack.translate(0, lerp, 0);
			guiGraphics.blit(TEXTURE, getX(), getY() - 14, 104 + u, 0, width, height);
		}
		else {
			poseStack.translate(0, -lerp, 0);
			guiGraphics.blit(TEXTURE, getX(), getY() + 16, 152 + u, 0, width, height);
		}

		poseStack.popPose();
		poseStack.pushPose();

		if(top) {
			poseStack.translate(0, lerp, 0);
			guiGraphics.renderItem(new ItemStack(item), getX() + 4, getY() - 2);
		}
		else {
			poseStack.translate(0, -lerp, 0);
			guiGraphics.renderItem(new ItemStack(item), getX() + 4, getY() + 24);
		}

		poseStack.popPose();
	}

	@Override
	public void onPress() {
		onPress.onPress(this);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
		defaultButtonNarrationText(narrationElementOutput);
	}

	public ResourceLocation getTabId() {
		return tabId;
	}

	public interface OnPress {
		void onPress(TabWidget widget);
	}
}
