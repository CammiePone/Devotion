package dev.cammiescorner.devotion.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.screens.ResearchScreenHandler;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ResearchScreen extends HandledScreen<ResearchScreenHandler> {
	public static final Identifier TEXTURE = Devotion.id("textures/gui/research/research_scroll.png");
	private final int z = 0;
	private final double angle = Math.toRadians(72);
	private final double offset = Math.PI * 0.5;
	private final int distance = 64;

	public ResearchScreen(ResearchScreenHandler handler, PlayerInventory inventory, Text text) {
		super(handler, inventory, text);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		DrawableHelper.drawTexture(matrices, x, y, z, 0, 0, 384, 216, 384, 320);
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		NbtList list = handler.getScroll().getOrCreateNbt().getList("RiddleList", NbtElement.STRING_TYPE);

		if(!list.isEmpty()) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.setShaderTexture(0, TEXTURE);
			DrawableHelper.drawTexture(matrices, 256, 104, z, 120, 216, 24, 24, 384, 320);

			for(int i = 0; i < 5; i++)
				DrawableHelper.drawTexture(matrices, pentagonX(256, i), pentagonY(104, i), z, i * 24, 216, 24, 24, 384, 320);
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected void init() {
		super.init();
		x = (width - 384) / 2;
		y = (height - 216) / 2;
		addDrawableChild(new ButtonWidget(x + 142, y + 200, 100, 20, Text.translatable("lectern." + Devotion.MOD_ID + ".take_scroll"), this::takeScrollButtonShit));
	}

	private void takeScrollButtonShit(ButtonWidget buttonWidget) {
		client.interactionManager.clickButton(handler.syncId, 0);
		closeScreen();
	}

	private int pentagonX(int x, int index) {
		return x + (int) (Math.cos(angle * index - offset) * distance);
	}

	private int pentagonY(int y, int index) {
		return y + (int) (Math.sin(angle * index - offset) * distance);
	}
}
