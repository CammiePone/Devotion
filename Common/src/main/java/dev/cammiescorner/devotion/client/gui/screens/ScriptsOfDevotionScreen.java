package dev.cammiescorner.devotion.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.book.BookEntry;
import dev.cammiescorner.devotion.api.book.BookTab;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.client.gui.widgets.ResearchWidget;
import dev.cammiescorner.devotion.client.gui.widgets.TabWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScriptsOfDevotionScreen extends Screen {
	public static final ResourceLocation TEXTURE = Devotion.id("textures/gui/scripts_of_devotion_frame.png");
	private final LinkedHashMap<ResourceLocation, Item> tabs = new LinkedHashMap<>();
	private final List<TabWidget> tabDrawables = new ArrayList<>();
	private final Map<ResearchWidget, BookTab> researchDrawables = new HashMap<>();
	private final RegistryAccess access = Minecraft.getInstance().player.registryAccess();
	public ResourceLocation tabId = Devotion.id("artifice");
	public int leftPos, topPos;
	public float offsetX, offsetY;

	public ScriptsOfDevotionScreen() {
		super(Component.empty());
	}

	@Override
	protected void init() {
		super.init();
		AtomicInteger tabCount = new AtomicInteger();
		leftPos = (width - 378) / 2;
		topPos = (height - 250) / 2;
		offsetX = DevotionClient.guideBookOffsetX;
		offsetY = DevotionClient.guideBookOffsetY;

		access.registryOrThrow(DevotionRegistries.BOOK_TAB).stream().sorted(Comparator.comparingInt(BookTab::order)).forEach(bookTab -> {
			if(tabCount.get() >= 26)
				return;

			ResourceLocation tabId = bookTab.getId(access);
			Item item = bookTab.icon().getItem();

			if(tabCount.get() < 13)
				addTabChild(new TabWidget(leftPos + 21 + (26 * tabCount.get()), topPos + 2, true, tabId, item, this::clickTab));
			else
				addTabChild(new TabWidget(leftPos + 21 + (26 * (tabCount.get() - 13)), topPos + 100, false, tabId, item, this::clickTab));

			tabCount.getAndIncrement();
		});
		access.registryOrThrow(DevotionRegistries.BOOK_ENTRY).forEach(bookEntry -> addResearchWidget(bookEntry, new ResearchWidget(leftPos + bookEntry.x(), topPos + bookEntry.y(), bookEntry.research().value().getId(access), DevotionClient::researchWidgetClick)));
	}

	@Override
	public void onClose() {
		DevotionClient.guideBookOffsetX = offsetX;
		DevotionClient.guideBookOffsetY = offsetY;
		super.onClose();
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
		drawWidgets(guiGraphics, poseStack, mouseX, mouseY, partialTick);
		poseStack.popPose();
		guiGraphics.disableScissor();

		drawForeground(guiGraphics);
		drawWidgetTooltips(guiGraphics, poseStack, mouseX, mouseY);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if(button == 0) {
			offsetX = (float) Mth.clamp(offsetX + dragX, -172, 172);
			offsetY = (float) Mth.clamp(offsetY + dragY, -108, 108);
		}

		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	protected void removeWidget(GuiEventListener listener) {
		super.removeWidget(listener);

		if(listener instanceof TabWidget)
			tabDrawables.remove(listener);
		if(listener instanceof ResearchWidget)
			researchDrawables.remove(listener);
	}

	@Override
	protected void clearWidgets() {
		super.clearWidgets();
		tabDrawables.clear();
		researchDrawables.clear();
	}

	protected void drawBackground(GuiGraphics guiGraphics) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 256, 378, 250, 512, 512);
	}

	protected void drawForeground(GuiGraphics guiGraphics) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		guiGraphics.blit(TEXTURE, leftPos, topPos, 110, 0, 0, 378, 250, 512, 512);
	}

	protected void drawWidgets(GuiGraphics guiGraphics, PoseStack poseStack, int mouseX, int mouseY, float delta) {
		poseStack.pushPose();
		poseStack.translate(-leftPos + offsetX, -topPos + offsetY, -200);

		for(ResearchWidget widget : researchDrawables.keySet()) {
			if(tabId.equals(researchDrawables.get(widget).getId(access))) {
				for(ResearchWidget parent : getParents(widget))
					drawLine(poseStack, parent.getX() + 15, parent.getY() + 15, widget.getX() + 15, widget.getY() + 15);
			}
		}

		for(ResearchWidget widget : researchDrawables.keySet()) {
			if(tabId.equals(researchDrawables.get(widget).getId(access))) {
				widget.setOffset(offsetX, offsetY, leftPos, topPos);
				widget.render(guiGraphics, mouseX, mouseY, delta);
			}
		}

		poseStack.popPose();

		poseStack.pushPose();
		poseStack.translate(-leftPos, -topPos, 0);

		for(TabWidget widget : tabDrawables)
			widget.render(guiGraphics, mouseX, mouseY, delta);

		poseStack.popPose();
	}

	protected void drawWidgetTooltips(GuiGraphics guiGraphics, PoseStack poseStack, int mouseX, int mouseY) {
		poseStack.pushPose();
		poseStack.translate(offsetX, offsetY, 0);

		for(ResearchWidget widget : researchDrawables.keySet()) {
			if(tabId.equals(researchDrawables.get(widget).getId(access)))
				widget.renderTooltip(guiGraphics, poseStack, mouseX, mouseY);
		}

		poseStack.popPose();
	}

	public <T extends TabWidget> T addTabChild(T drawable) {
		tabDrawables.add(drawable);
		return addWidget(drawable);
	}

	public <T extends ResearchWidget> void addResearchWidget(BookEntry bookEntry, T drawable) {
		researchDrawables.put(drawable, bookEntry.tab().value());
		addWidget(drawable);
	}

	private void drawLine(PoseStack poseStack, float x1, float y1, float x2, float y2) {
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(0.224f, 0.196f, 0.175f, 1f);
		Matrix4f matrix = poseStack.last().pose();
		Vec2 startPos = new Vec2(x1, y1);
		Vec2 midPos = new Vec2(x1 + (x2 - x1) * 0.5f, y1 + (y2 - y1) * 0.5f);
		Vec2 endPos = new Vec2(x2, y2);
		float angle = (float) (Math.atan2(y2 - y1, x2 - x1) - (Math.PI * 0.5));
		float prevDelta = 0;
		int segmentCount = 8;

		if(startPos.x == endPos.x || startPos.y == endPos.y)
			segmentCount = 1;

		if(segmentCount > 1) {
			float offset = Mth.sqrt(startPos.distanceToSqr(endPos)) * 0.25f;
			float angleDeg = (float) Math.toDegrees(angle) + 270;

			// top
			if(angleDeg < 90 && angleDeg > 45)
				midPos = midPos.add(new Vec2(-offset, 0));
			if(angleDeg < 135 && angleDeg > 90)
				midPos = midPos.add(new Vec2(offset, 0));

			// right
			if(angleDeg < 180 && angleDeg > 135)
				midPos = midPos.add(new Vec2(0, -offset));
			if(angleDeg < 225 && angleDeg > 180)
				midPos = midPos.add(new Vec2(0, offset));

			// bottom
			if(angleDeg < 270 && angleDeg > 225)
				midPos = midPos.add(new Vec2(offset, 0));
			if(angleDeg < 315 && angleDeg > 270)
				midPos = midPos.add(new Vec2(-offset, 0));

			// left
			if(angleDeg < 360 && angleDeg > 315)
				midPos = midPos.add(new Vec2(0, offset));
			if(angleDeg < 45 && angleDeg > 0)
				midPos = midPos.add(new Vec2(0, -offset));
		}

		for(int i = 1; i <= segmentCount; i++) {
			float delta = i / (float) segmentCount;
			Vec2 a1 = lerp(prevDelta, startPos, midPos);
			Vec2 b1 = lerp(prevDelta, midPos, endPos);
			Vec2 c1 = lerp(prevDelta, a1, b1);
			Vec2 a2 = lerp(delta, startPos, midPos);
			Vec2 b2 = lerp(delta, midPos, endPos);
			Vec2 c2 = lerp(delta, a2, b2);
			float angle2 = (float) (Mth.atan2(c2.y - c1.y, c2.x - c1.x) - (Math.PI * 0.5));
			float dx = Mth.cos(angle2) * 2;
			float dy = Mth.sin(angle2) * 2;

			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			bufferBuilder.addVertex(matrix, c2.x - dx, c2.y - dy, 0).setColor(0);
			bufferBuilder.addVertex(matrix, c2.x + dx, c2.y + dy, 0).setColor(0);
			bufferBuilder.addVertex(matrix, c1.x + dx, c1.y + dy, 0).setColor(0);
			bufferBuilder.addVertex(matrix, c1.x - dx, c1.y - dy, 0).setColor(0);
			MeshData data = bufferBuilder.build();

			if(data != null)
				BufferUploader.drawWithShader(data);

			prevDelta = delta - 0.004f;
		}
	}

	private Vec2 lerp(float delta, Vec2 pos1, Vec2 pos2) {
		return pos1.scale(1 - delta).add(pos2.scale(delta));
	}

	private List<ResearchWidget> getParents(ResearchWidget widget) {
		List<ResearchWidget> parents = new ArrayList<>();

		if(widget.visible) {
			Holder.Reference<Research> research = widget.getResearch();

			for(ResearchWidget parent : researchDrawables.keySet())
				if(parent.visible && research.value().parentIds().contains(parent.getResearch().key().location()))
					parents.add(parent);
		}

		return parents;
	}

	private void clickTab(TabWidget widget) {
		tabId = widget.getTabId();
	}
}
