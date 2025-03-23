package dev.cammiescorner.devotion.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.gui.screens.ScriptsOfDevotionScreen;
import net.minecraft.Util;
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
	private int topScrollOffset, bottomScrollOffset;
	private float yPos;

	public TabWidget(int x, int y, boolean top, ResourceLocation tabId, Item item, OnPress onPress) {
		super(x, y, 24, 40, Component.empty());
		this.top = top;
		this.tabId = tabId;
		this.item = item;
		this.onPress = onPress;
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return active && visible && isHovered;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return active && visible && isHovered;
	}

	@Override
	public boolean isFocused() {
		return Minecraft.getInstance().screen instanceof ScriptsOfDevotionScreen guideBookScreen && guideBookScreen.tabId.equals(tabId);
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		PoseStack poseStack = guiGraphics.pose();
		isHovered = Minecraft.getInstance().screen instanceof ScriptsOfDevotionScreen screen && isInsideBorder(screen, mouseX, mouseY) && mouseX >= getX() + getScrollOffset() && mouseY >= getY() + getOffsetY() && mouseX < getX() + getScrollOffset() + width && mouseY < getY() + height;
		int u = 0;

		if(isHoveredOrFocused()) {
			yPos = Math.min(10, yPos + 1);
		}
		else {
			u = 24;
			yPos = Math.max(0, yPos - 1);
		}

		float lerp = Mth.lerp(yPos / 10f, 0f, 17f);
		poseStack.pushPose();

		if(top) {
			poseStack.translate(topScrollOffset, lerp, 0);
			guiGraphics.blit(TEXTURE, getX(), getY() - 14, 104 + u, 0, width, height);
			guiGraphics.renderItem(new ItemStack(item), getX() + 4, getY() - 2);
		}
		else {
			poseStack.translate(bottomScrollOffset, -lerp, 0);
			guiGraphics.blit(TEXTURE, getX(), getY() + 25, 152 + u, 0, width, height);
			guiGraphics.renderItem(new ItemStack(item), getX() + 4, getY() + 34);
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

	public void renderTooltip(GuiGraphics guiGraphics, PoseStack poseStack, int mouseX, int mouseY) {
		if(Minecraft.getInstance().screen instanceof ScriptsOfDevotionScreen screen && isHovered() && isInsideBorder(screen, mouseX, mouseY)) {
			poseStack.pushPose();
			poseStack.translate(-getScrollOffset(), 0, 0);
			guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(Util.makeDescriptionId("devotion_tab", tabId)), mouseX, mouseY);
			poseStack.popPose();
		}
	}

	private boolean isInsideBorder(ScriptsOfDevotionScreen screen, int mouseX, int mouseY) {
		return mouseX >= screen.leftPos + 16 && mouseY >= screen.topPos + 16 && mouseX < screen.leftPos + 362 && mouseY < screen.topPos + 234;
	}

	public void setScrollOffsets(int topOffset, int bottomOffset) {
		topScrollOffset = topOffset;
		bottomScrollOffset = bottomOffset;
	}

	public int getScrollOffset() {
		return top ? topScrollOffset : bottomScrollOffset;
	}

	public int getOffsetY() {
		return top ? 15 : 8;
	}

	public boolean isTop() {
		return top;
	}

	public ResourceLocation getTabId() {
		return tabId;
	}

	public interface OnPress {
		void onPress(TabWidget widget);
	}
}
