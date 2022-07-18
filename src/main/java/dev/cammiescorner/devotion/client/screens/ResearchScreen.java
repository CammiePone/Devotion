package dev.cammiescorner.devotion.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
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
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;

public class ResearchScreen extends HandledScreen<ResearchScreenHandler> {
	public static final Identifier TEXTURE = Devotion.id("textures/gui/research/research_scroll.png");
	private final List<Pair<Vec2f, Vec2f>> lines = new ArrayList<>();
	private final Vec2f enhancerPos = new Vec2f(268, 52);
	private final Vec2f transmuterPos = new Vec2f(328, 97);
	private final Vec2f conjurerPos = new Vec2f(305, 167);
	private final Vec2f manipulatorPos = new Vec2f(231, 167);
	private final Vec2f emitterPos = new Vec2f(208, 97);
	private final Vec2f specialistPos = new Vec2f(268, 116);
	private final double angle = Math.toRadians(72);
	private final double offset = Math.PI * 0.5;
	private final int distance = 64;
	private final int z = 0;
	private Vec2f mousePos = new Vec2f(0, 0);
	private Vec2f lineStart;

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
		if(lineStart != null)
			drawLine(matrices, lineStart.x, lineStart.y, mousePos.x, mousePos.y);

		if(!lines.isEmpty()) {
			for(Pair<Vec2f, Vec2f> line : lines) {
				Vec2f startPos = line.getLeft();
				Vec2f endPos = line.getRight();
				drawLine(matrices, startPos.x, startPos.y, endPos.x, endPos.y);
			}
		}

		NbtList list = handler.getScroll().getOrCreateNbt().getList("RiddleList", NbtElement.STRING_TYPE);

		if(!list.isEmpty()) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.setShaderTexture(0, TEXTURE);
			DrawableHelper.drawTexture(matrices, 256, 104, z, 120, 216, 24, 24, 384, 320);

			for(int i = 0; i < 5; i++)
				DrawableHelper.drawTexture(matrices, pentagonX(256, i), pentagonY(104, i), z, i * 24, 216, 24, 24, 384, 320);

			for(int i = 0; i < list.size(); i++)
				textRenderer.draw(matrices, list.getString(i), 48, 40 + 16 * i, 0);
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

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(lineStart == null) {
			if(mousePos.distanceSquared(enhancerPos) <= 144)
				lineStart = enhancerPos;
			else if(mousePos.distanceSquared(transmuterPos) <= 144)
				lineStart = transmuterPos;
			else if(mousePos.distanceSquared(emitterPos) <= 144)
				lineStart = emitterPos;
			else if(mousePos.distanceSquared(conjurerPos) <= 144)
				lineStart = conjurerPos;
			else if(mousePos.distanceSquared(manipulatorPos) <= 144)
				lineStart = manipulatorPos;
			else if(mousePos.distanceSquared(specialistPos) <= 144)
				lineStart = specialistPos;
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(lineStart != null) {
			if(mousePos.distanceSquared(enhancerPos) <= 144 && !lineStart.equals(enhancerPos))
				lines.add(new Pair<>(lineStart, enhancerPos));
			else if(mousePos.distanceSquared(transmuterPos) <= 144 && !lineStart.equals(transmuterPos))
				lines.add(new Pair<>(lineStart, transmuterPos));
			else if(mousePos.distanceSquared(emitterPos) <= 144 && !lineStart.equals(emitterPos))
				lines.add(new Pair<>(lineStart, emitterPos));
			else if(mousePos.distanceSquared(conjurerPos) <= 144 && !lineStart.equals(conjurerPos))
				lines.add(new Pair<>(lineStart, conjurerPos));
			else if(mousePos.distanceSquared(manipulatorPos) <= 144 && !lineStart.equals(manipulatorPos))
				lines.add(new Pair<>(lineStart, manipulatorPos));
			else if(mousePos.distanceSquared(specialistPos) <= 144 && !lineStart.equals(specialistPos))
				lines.add(new Pair<>(lineStart, specialistPos));

			lineStart = null;
		}

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		super.mouseMoved(mouseX, mouseY);
		mousePos = new Vec2f((float) (mouseX - x), (float) (mouseY - y));
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

	private void drawLine(MatrixStack matrices, float x1, float y1, float x2, float y2) {
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(0.4F, 1F, 0.8F, 1F);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();
		Matrix4f matrix = matrices.peek().getPosition();
		float angle = (float) (Math.atan2(y2 - y1, x2 - x1) - (Math.PI * 0.5));
		float dx = MathHelper.cos(angle) * 2;
		float dy = MathHelper.sin(angle) * 2;

		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(matrix, x2 - dx, y2 - dy, 0).color(0).next();
		bufferBuilder.vertex(matrix, x2 + dx, y2 + dy, 0).color(0).next();
		bufferBuilder.vertex(matrix, x1 + dx, y1 + dy, 0).color(0).next();
		bufferBuilder.vertex(matrix, x1 - dx, y1 - dy, 0).color(0).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
	}
}
