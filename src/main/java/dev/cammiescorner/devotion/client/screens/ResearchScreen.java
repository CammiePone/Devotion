package dev.cammiescorner.devotion.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.packets.c2s.SaveScrollDataPacket;
import dev.cammiescorner.devotion.common.screens.ResearchScreenHandler;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.OrderedText;
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
	private final List<AuraType> auraTypes = new ArrayList<>();
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
	private Vec2f lastPos = new Vec2f(0, 0);
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

		NbtCompound tag = handler.getScroll().getSubNbt(Devotion.MOD_ID);

		if(tag != null) {
			NbtList list = tag.getList("RiddleList", NbtElement.COMPOUND_TYPE);

			if(!list.isEmpty()) {
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				RenderSystem.setShaderTexture(0, TEXTURE);
				DrawableHelper.drawTexture(matrices, 256, 104, z, 120, 216, 24, 24, 384, 320);

				for(int i = 0; i < 5; i++)
					DrawableHelper.drawTexture(matrices, pentagonX(256, i), pentagonY(104, i), z, i * 24, 216, 24, 24, 384, 320);

				List<OrderedText> agony = new ArrayList<>();
				int posY = 0;
				int boxHeight = 151;
				int boxWidth = 132;

				for(int i = 0; i < list.size(); i++) {
					NbtCompound compound = list.getCompound(i);
					AuraType type = AuraType.values()[compound.getInt("AuraTypeIndex")];

					agony.addAll(textRenderer.wrapLines(Text.translatable("riddle_text." + type.getName() + "." + compound.getInt("RiddleIndex")), boxWidth));

					if(i < list.size() - 1)
						agony.add(Text.literal("").asOrderedText());
				}

				matrices.push();
				float textHeight = Math.max(1, agony.size()) * Math.max(1, textRenderer.fontHeight - 1);
				float scale = Math.min(1, boxHeight / textHeight);
				matrices.translate(48 + (boxWidth * 0.5), 34 + (boxHeight * 0.5), 0);
				matrices.scale(scale, scale, 1);
				matrices.translate(0, -textHeight * 0.5, 0);

				for(OrderedText text : agony) {
					textRenderer.draw(matrices, text, -textRenderer.getWidth(text) * 0.5F, posY, 0);
					posY += 8;
				}

				matrices.pop();
			}
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
		redrawLines();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		NbtCompound tag = handler.getScroll().getSubNbt(Devotion.MOD_ID);

		if(tag != null && !tag.getBoolean("Completed")) {
			NbtList list = tag.getList("RiddleList", NbtElement.COMPOUND_TYPE);

			if(!list.isEmpty() && lineStart == null && button == 0) {
				if(!lines.isEmpty())
					lastPos = lines.get(lines.size() - 1).getRight();

				if(mousePos.distanceSquared(enhancerPos) <= 144 && (lines.isEmpty() || lastPos.equals(enhancerPos))) {
					lineStart = enhancerPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.ENHANCER);
				}
				else if(mousePos.distanceSquared(transmuterPos) <= 144 && (lines.isEmpty() || lastPos.equals(transmuterPos))) {
					lineStart = transmuterPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.TRANSMUTER);
				}
				else if(mousePos.distanceSquared(emitterPos) <= 144 && (lines.isEmpty() || lastPos.equals(emitterPos))) {
					lineStart = emitterPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.EMITTER);
				}
				else if(mousePos.distanceSquared(conjurerPos) <= 144 && (lines.isEmpty() || lastPos.equals(conjurerPos))) {
					lineStart = conjurerPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.CONJURER);
				}
				else if(mousePos.distanceSquared(manipulatorPos) <= 144 && (lines.isEmpty() || lastPos.equals(manipulatorPos))) {
					lineStart = manipulatorPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.MANIPULATOR);
				}
				else if(mousePos.distanceSquared(specialistPos) <= 144 && (lines.isEmpty() || lastPos.equals(specialistPos))) {
					lineStart = specialistPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.SPECIALIST);
				}
			}

			if(button == 1) {
				if(!lines.isEmpty())
					lines.remove(lines.size() - 1);
				if(!auraTypes.isEmpty())
					auraTypes.remove(auraTypes.size() - 1);

				SaveScrollDataPacket.send(handler.syncId, auraTypes);
				lineStart = null;
				lastPos = new Vec2f(0, 0);
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(lineStart != null && button == 0) {
			if(mousePos.distanceSquared(enhancerPos) <= 144 && !lineStart.equals(enhancerPos))
				addLine(lineStart, enhancerPos, AuraType.ENHANCER);
			else if(mousePos.distanceSquared(transmuterPos) <= 144 && !lineStart.equals(transmuterPos))
				addLine(lineStart, transmuterPos, AuraType.TRANSMUTER);
			else if(mousePos.distanceSquared(emitterPos) <= 144 && !lineStart.equals(emitterPos))
				addLine(lineStart, emitterPos, AuraType.EMITTER);
			else if(mousePos.distanceSquared(conjurerPos) <= 144 && !lineStart.equals(conjurerPos))
				addLine(lineStart, conjurerPos, AuraType.CONJURER);
			else if(mousePos.distanceSquared(manipulatorPos) <= 144 && !lineStart.equals(manipulatorPos))
				addLine(lineStart, manipulatorPos, AuraType.MANIPULATOR);
			else if(mousePos.distanceSquared(specialistPos) <= 144 && !lineStart.equals(specialistPos))
				addLine(lineStart, specialistPos, AuraType.SPECIALIST);

			lineStart = null;
			SaveScrollDataPacket.send(handler.syncId, auraTypes);
		}

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		super.mouseMoved(mouseX, mouseY);
		mousePos = new Vec2f((float) (mouseX - x), (float) (mouseY - y));
	}

	@Override
	public void closeScreen() {
		SaveScrollDataPacket.send(handler.syncId, auraTypes);
		super.closeScreen();
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

	public void redrawLines() {
		NbtCompound tag = handler.getScroll().getSubNbt(Devotion.MOD_ID);

		if(tag != null) {
			NbtList nbtList = tag.getList("AuraTypes", NbtElement.INT_TYPE);
			boolean completed = tag.getBoolean("Completed");
			// TODO if completed, render the aura shader around the lines @Will BL
			lines.clear();
			auraTypes.clear();
			lineStart = null;

			if(!nbtList.isEmpty()) {
				for(int i = 0; i < nbtList.size() - 1; i++) {
					AuraType current = AuraType.values()[nbtList.getInt(i)];
					AuraType next = AuraType.values()[nbtList.getInt(i + 1)];
					Vec2f lineX = switch(current) {
						case ENHANCER -> enhancerPos;
						case TRANSMUTER -> transmuterPos;
						case EMITTER -> emitterPos;
						case CONJURER -> conjurerPos;
						case MANIPULATOR -> manipulatorPos;
						case SPECIALIST -> specialistPos;
						case NONE -> new Vec2f(0, 0);
					};
					Vec2f lineY = switch(next) {
						case ENHANCER -> enhancerPos;
						case TRANSMUTER -> transmuterPos;
						case EMITTER -> emitterPos;
						case CONJURER -> conjurerPos;
						case MANIPULATOR -> manipulatorPos;
						case SPECIALIST -> specialistPos;
						case NONE -> new Vec2f(0, 0);
					};

					lines.add(new Pair<>(lineX, lineY));
					auraTypes.add(current);

					if(i == nbtList.size() - 2)
						auraTypes.add(next);
				}
			}
		}
	}

	public void addLine(Vec2f pos1, Vec2f pos2, AuraType type) {
		for(Pair<Vec2f, Vec2f> line : lines)
			if((line.getLeft().equals(pos1) && line.getRight().equals(pos2)) || (line.getLeft().equals(pos2) && line.getRight().equals(pos1)))
				return;

		lines.add(new Pair<>(pos1, pos2));
		auraTypes.add(type);
	}
}
