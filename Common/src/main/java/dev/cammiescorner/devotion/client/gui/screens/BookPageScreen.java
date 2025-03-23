package dev.cammiescorner.devotion.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.api.book.BookPage;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.client.gui.widgets.ArrowWidget;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BookPageScreen extends Screen {
	private final List<ArrowWidget> arrowWidgets = new ArrayList<>();
	private final List<BookPage> pages = new ArrayList<>();
	public int leftPos, topPos, index;

	public BookPageScreen(Holder.Reference<Research> research) {
		super(Component.translatable(Util.makeDescriptionId("devotion_research", research.key().location())));
		this.pages.addAll(research.value().pages());
	}

	@Override
	protected void init() {
		super.init();
		leftPos = (width - 378) / 2;
		topPos = (height - 250) / 2;

		addArrowWidget(new ArrowWidget(leftPos + 150, topPos + 235, true, this::clickPrev));
		addArrowWidget(new ArrowWidget(leftPos + 204, topPos + 235, false, this::clickNext));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		PoseStack poseStack = guiGraphics.pose();

		drawBackground(guiGraphics);

		guiGraphics.enableScissor(leftPos + 16, topPos + 16, leftPos + 362, topPos + 234);
		poseStack.pushPose();
		poseStack.translate(leftPos, topPos, 0);

		super.render(guiGraphics, mouseX, mouseY, partialTick);
		drawContents(guiGraphics);

		poseStack.popPose();
		guiGraphics.disableScissor();

		drawForeground(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	protected void clearWidgets() {
		super.clearWidgets();
		arrowWidgets.clear();
	}

	@Override
	protected void removeWidget(GuiEventListener listener) {
		super.removeWidget(listener);

		if(listener instanceof ArrowWidget)
			arrowWidgets.remove(listener);
	}

	@Override
	public void onClose() {
		DevotionClient.lastGuideBookScreen = this;
		super.onClose();
	}

	@Override
	public Component getNarrationMessage() {
		return Component.translatable(pages.get(index).text());
	}

	public <T extends ArrowWidget> void addArrowWidget(T drawable) {
		arrowWidgets.add(drawable);
		addWidget(drawable);
	}

	private void drawBackground(GuiGraphics guiGraphics) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		guiGraphics.blit(ScriptsOfDevotionScreen.TEXTURE, leftPos, topPos, 0, 256, 378, 250, 512, 512);
	}

	private void drawForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		guiGraphics.blit(ScriptsOfDevotionScreen.TEXTURE, leftPos, topPos, 200, 0, 0, 378, 250, 512, 512);

		for(ArrowWidget widget : arrowWidgets) {
			widget.active = widget.pointsLeft || index < pages.size() - 1;
			widget.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	private void drawContents(GuiGraphics guiGraphics) {
		BookPage page = pages.get(index);
		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();
		poseStack.translate((378 / 2f) - ((font.width(title.getString()) * 1.25f) / 2f), 24f, 0f);
		poseStack.scale(1.25f, 1.25f, 1f);

		font.drawInBatch(title, 0, 0, 0x000000, false, poseStack.last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0xffffff, 0);

		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(24f, 48f, 1f);
		poseStack.scale(0.75f, 0.75f, 1f);

		// TODO format main body text & pictures
		font.drawInBatch(Component.translatable(page.text()), 0, 0, 0x000000, false, poseStack.last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0xffffff, 0);

		poseStack.popPose();
	}

	private void clickNext(ArrowWidget arrowWidget) {
		index = pages.isEmpty() ? 0 : Math.min(index + 1, pages.size() - 1);
	}

	private void clickPrev(ArrowWidget arrowWidget) {
		if(index <= 0)
			Minecraft.getInstance().setScreen(new ScriptsOfDevotionScreen());

		index = Math.max(index - 1, 0);
	}
}