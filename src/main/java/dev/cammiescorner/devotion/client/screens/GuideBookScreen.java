package dev.cammiescorner.devotion.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.devotion.Devotion;
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
		addArtificeChild(new ResearchWidget(x + 174, y + 110, Devotion.id("temp1"), widget -> System.out.println("beep")));
		addArtificeChild(new ResearchWidget(x + 214, y + 110, Devotion.id("temp2"), widget -> System.out.println("boop")));
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
			boolean isArtificeTab = tabId.equals(Devotion.id("artifice"));
			boolean isSpellsTab = tabId.equals(Devotion.id("spells"));
			boolean isCultsTab = tabId.equals(Devotion.id("cults"));
			int scale = (int) client.getWindow().getScaleFactor();
			RenderSystem.enableScissor((x + 16) * scale, (y + 16) * scale, 346 * scale, 218 * scale);
			matrices.push();
			matrices.translate(-x, -y, 0);

			for(ResearchWidget widget : artificeDrawables) {
				widget.setOffset(offsetX, offsetY);
				widget.render(matrices, mouseX, mouseY, client.getTickDelta());
				widget.visible = isArtificeTab;
			}

			for(ResearchWidget widget : spellDrawables) {
				widget.setOffset(offsetX, offsetY);
				widget.render(matrices, mouseX, mouseY, client.getTickDelta());
				widget.visible = isSpellsTab;
			}

			for(ResearchWidget widget : cultDrawables) {
				widget.setOffset(offsetX, offsetY);
				widget.render(matrices, mouseX, mouseY, client.getTickDelta());
				widget.visible = isCultsTab;
			}

			matrices.pop();
			RenderSystem.disableScissor();
		}

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 378, 250, 512, 512);
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
}
