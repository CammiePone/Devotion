package dev.cammiescorner.devotion.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.events.client.ResearchWidgetCallback;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.client.widgets.ResearchWidget;
import dev.cammiescorner.devotion.common.screens.GuideBookScreenHandler;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;

public class GuideBookScreen extends HandledScreen<GuideBookScreenHandler> {
	public static final Identifier TEXTURE = Devotion.id("textures/gui/scripts_of_devotion_frame.png");
	private final List<ResearchWidget> artificeDrawables = new ArrayList<>();
	private final List<ResearchWidget> spellDrawables = new ArrayList<>();
	private final List<ResearchWidget> cultDrawables = new ArrayList<>();
	public Identifier tabId = Devotion.id("artifice");
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
		ResearchWidgetCallback.ADD_WIDGETS.invoker().addWidgets(this, x, y);
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
			matrices.translate(-x, -y, 0);

			if(tabId.equals(Devotion.id("artifice"))) {
				for(ResearchWidget widget : artificeDrawables)
					for(ResearchWidget parent : getParents(widget, artificeDrawables))
						drawLine(matrices, (float) (parent.x + offsetX + 15), (float) (parent.y + offsetY + 15), (float) (widget.x + offsetX + 15), (float) (widget.y + offsetY + 15));

				for(ResearchWidget widget : artificeDrawables) {
					widget.setOffset(offsetX, offsetY);
					widget.render(matrices, mouseX, mouseY, client.getTickDelta());
				}
			}

			if(tabId.equals(Devotion.id("spells"))) {
				for(ResearchWidget widget : spellDrawables)
					for(ResearchWidget parent : getParents(widget, spellDrawables))
						drawLine(matrices, (float) (parent.x + offsetX + 15), (float) (parent.y + offsetY + 15), (float) (widget.x + offsetX + 15), (float) (widget.y + offsetY + 15));

				for(ResearchWidget widget : spellDrawables) {
					widget.setOffset(offsetX, offsetY);
					widget.render(matrices, mouseX, mouseY, client.getTickDelta());
				}
			}

			if(tabId.equals(Devotion.id("cults"))) {
				for(ResearchWidget widget : cultDrawables)
					for(ResearchWidget parent : getParents(widget, cultDrawables))
						drawLine(matrices, (float) (parent.x + offsetX + 15), (float) (parent.y + offsetY + 15), (float) (widget.x + offsetX + 15), (float) (widget.y + offsetY + 15));

				for(ResearchWidget widget : cultDrawables) {
					widget.setOffset(offsetX, offsetY);
					widget.render(matrices, mouseX, mouseY, client.getTickDelta());
				}
			}

			matrices.pop();
			RenderSystem.disableScissor();
		}

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		DrawableHelper.drawTexture(matrices, 0, 0, 101, 0, 0, 378, 250, 512, 512);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if(button == 0) {
			offsetX = MathHelper.clamp(offsetX + deltaX, -172, 172);
			offsetY = MathHelper.clamp(offsetY + deltaY, -108, 108);
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	protected void remove(Element child) {
		super.remove(child);

		if(child instanceof ResearchWidget) {
			artificeDrawables.remove(child);
			spellDrawables.remove(child);
			cultDrawables.remove(child);
		}
	}

	@Override
	protected void clearChildren() {
		super.clearChildren();
		artificeDrawables.clear();
		spellDrawables.clear();
		cultDrawables.clear();
	}

	public <T extends ResearchWidget> T addArtificeChild(T drawable) {
		artificeDrawables.add(drawable);
		return this.addSelectableChild(drawable);
	}

	public <T extends ResearchWidget> T addSpellChild(T drawable) {
		spellDrawables.add(drawable);
		return this.addSelectableChild(drawable);
	}

	public <T extends ResearchWidget> T addCultChild(T drawable) {
		cultDrawables.add(drawable);
		return this.addSelectableChild(drawable);
	}

	private void drawLine(MatrixStack matrices, float x1, float y1, float x2, float y2) {
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(0.224F, 0.196F, 0.175F, 1F);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();
		Matrix4f matrix = matrices.peek().getPosition();
		float angle = (float) (Math.atan2(y2 - y1, x2 - x1) - (Math.PI * 0.5));
		int segmentCount = 16;
		Vec2f pos1 = new Vec2f(x1, y1);
		Vec2f pos2 = new Vec2f(x2, y2);

		if(pos1.x == pos2.x || pos1.y == pos2.y)
			segmentCount = 1;

		for(int i = 1; i <= segmentCount; i++) {
			float dx = MathHelper.cos(i * angle / segmentCount) * 2;
			float dy = MathHelper.sin(i * angle / segmentCount) * 2;

			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
			bufferBuilder.vertex(matrix, x2 - dx, y2 - dy, 0).color(0).next();
			bufferBuilder.vertex(matrix, x2 + dx, y2 + dy, 0).color(0).next();
			bufferBuilder.vertex(matrix, x1 + dx, y1 + dy, 0).color(0).next();
			bufferBuilder.vertex(matrix, x1 - dx, y1 - dy, 0).color(0).next();
			BufferRenderer.drawWithShader(bufferBuilder.end());
		}
	}

	private List<ResearchWidget> getParents(ResearchWidget widget, List<ResearchWidget> drawables) {
		List<ResearchWidget> parents = new ArrayList<>();

		if(widget.visible) {
			Research research = widget.getResearch();

			for(ResearchWidget parent : drawables)
				if(parent.visible && research.getParents().contains(parent.getResearch()))
					parents.add(parent);
		}

		return parents;
	}
}
