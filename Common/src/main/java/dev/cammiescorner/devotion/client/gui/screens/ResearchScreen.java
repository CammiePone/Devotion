package dev.cammiescorner.devotion.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.RiddleData;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.networking.serverbound.ServerboundSaveScrollDataPacket;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.screens.ResearchMenu;
import dev.upcraft.sparkweave.api.color.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResearchScreen extends AbstractContainerScreen<ResearchMenu> {
	public static final ResourceLocation TEXTURE = Devotion.id("textures/gui/research/research_scroll.png");
	private final List<Pair<Vec2, Vec2>> lines = new ArrayList<>();
	private final List<AuraType> auraTypes = new ArrayList<>();
	private final List<Vec2> postions = Arrays.asList(
		new Vec2(268, 52),  // top
		new Vec2(328, 97),  // top right
		new Vec2(305, 167), // bottom right
		new Vec2(231, 167), // bottom left
		new Vec2(208, 97),  // top left
		new Vec2(268, 116)  // center
	);
	private Vec2 enhancerPos = Vec2.ZERO;
	private Vec2 transmuterPos = Vec2.ZERO;
	private Vec2 conjurerPos = Vec2.ZERO;
	private Vec2 manipulatorPos = Vec2.ZERO;
	private Vec2 emitterPos = Vec2.ZERO;
	private Vec2 specialistPos = Vec2.ZERO;
	private final double angle = Math.toRadians(72);
	private final double offset = Math.PI * 0.5;
	private final int distance = 64;
	private ItemStack stack = ItemStack.EMPTY;
	private Vec2 mousePos = new Vec2(0, 0);
	private Vec2 lastPos = new Vec2(0, 0);
	private Vec2 lineStart;

	public ResearchScreen(ResearchMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, Component.empty());
	}

	@Override
	protected void init() {
		super.init();
		leftPos = (width - 384) / 2;
		topPos = (height - 216) / 2;
		Collections.shuffle(postions);
		enhancerPos = postions.get(0);
		transmuterPos = postions.get(1);
		conjurerPos = postions.get(2);
		manipulatorPos = postions.get(3);
		emitterPos = postions.get(4);
		specialistPos = postions.get(5);

		addRenderableWidget(Button.builder(Component.translatable("lectern." + Devotion.MOD_ID + ".take_scroll"), this::takeScrollButtonShit).bounds(leftPos + 142, topPos + 200, 100, 20).build());
		redrawLines();
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, 384, 216, 384, 320);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		PoseStack poseStack = guiGraphics.pose();
		Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
		int guiScale = (int) Minecraft.getInstance().getWindow().getGuiScale();

		matrixStack.pushMatrix();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableScissor((leftPos + 194) * guiScale, (topPos + 35) * guiScale, 148 * guiScale, 144 * guiScale);

		if(lineStart != null)
			drawLine(poseStack, lineStart.x, lineStart.y, mousePos.x, mousePos.y);

		if(!lines.isEmpty()) {
			for(Pair<Vec2, Vec2> line : lines) {
				Vec2 startPos = line.getFirst();
				Vec2 endPos = line.getSecond();
				drawLine(poseStack, startPos.x, startPos.y, endPos.x, endPos.y);
			}
		}

		RenderSystem.disableScissor();
		matrixStack.popMatrix();
		RenderSystem.applyModelViewMatrix();

		DataComponentType<RiddleData> riddleData = DevotionData.RIDDLE_DATA.get();

		if(stack.get(riddleData) instanceof RiddleData riddles) {
			List<Pair<AuraType, Integer>> list = riddles.riddles();

			if(!list.isEmpty()) {
				RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

				for(int i = 0; i < postions.size(); i++) {
					Vec2 position = postions.get(i);
					guiGraphics.blit(TEXTURE, (int) position.x - 12, (int) position.y - 12, i * 24, 216, 24, 24, 384, 320);
				}

				List<FormattedCharSequence> agony = new ArrayList<>();
				int posY = 0;
				int boxHeight = 151;
				int boxWidth = 132;

				for(int i = 0; i < list.size(); i++) {
					agony.addAll(font.split(Component.translatable(riddles.getRiddleTranslationKey(i)), boxWidth));

					if(i < list.size() - 1)
						agony.add(Component.literal("").getVisualOrderText());
				}

				poseStack.pushPose();
				float textHeight = Math.max(1, agony.size()) * Math.max(1, font.lineHeight - 1);
				float scale = Math.min(1, boxHeight / textHeight);
				poseStack.translate(48 + (boxWidth * 0.5), 34 + (boxHeight * 0.5), 0);
				poseStack.scale(scale, scale, 1);
				poseStack.translate(0, -textHeight * 0.5, 0);

				for(FormattedCharSequence text : agony) {
					guiGraphics.drawString(font, text, (int) (-font.width(text) * 0.5f), posY, 0, false);
					posY += 8;
				}

				poseStack.popPose();
			}
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderMenuBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		DataComponentType<Boolean> completedData = DevotionData.SCROLL_COMPLETED.get();
		DataComponentType<RiddleData> riddleData = DevotionData.RIDDLE_DATA.get();

		if(!stack.getOrDefault(completedData, false)) {
			RiddleData riddles = stack.get(riddleData);

			if(riddles != null && !riddles.riddles().isEmpty() && lineStart == null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				if(!lines.isEmpty())
					lastPos = lines.getLast().getSecond();

				if(mousePos.distanceToSqr(enhancerPos) <= 144 && (lines.isEmpty() || lastPos.equals(enhancerPos))) {
					lineStart = enhancerPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.ENHANCEMENT);
				}
				else if(mousePos.distanceToSqr(transmuterPos) <= 144 && (lines.isEmpty() || lastPos.equals(transmuterPos))) {
					lineStart = transmuterPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.TRANSMUTATION);
				}
				else if(mousePos.distanceToSqr(emitterPos) <= 144 && (lines.isEmpty() || lastPos.equals(emitterPos))) {
					lineStart = emitterPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.EMISSION);
				}
				else if(mousePos.distanceToSqr(conjurerPos) <= 144 && (lines.isEmpty() || lastPos.equals(conjurerPos))) {
					lineStart = conjurerPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.CONJURATION);
				}
				else if(mousePos.distanceToSqr(manipulatorPos) <= 144 && (lines.isEmpty() || lastPos.equals(manipulatorPos))) {
					lineStart = manipulatorPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.MANIPULATION);
				}
				else if(mousePos.distanceToSqr(specialistPos) <= 144 && (lines.isEmpty() || lastPos.equals(specialistPos))) {
					lineStart = specialistPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.SPECIALIZATION);
				}
			}

			if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				if(!lines.isEmpty())
					lines.removeLast();
				if(!auraTypes.isEmpty())
					auraTypes.removeLast();

				Network.getNetworkHandler().sendToServer(new ServerboundSaveScrollDataPacket(menu.containerId, auraTypes));
				lineStart = null;
				lastPos = new Vec2(0, 0);
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(lineStart != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(mousePos.distanceToSqr(enhancerPos) <= 144 && !lineStart.equals(enhancerPos))
				addLine(lineStart, enhancerPos, AuraType.ENHANCEMENT);
			else if(mousePos.distanceToSqr(transmuterPos) <= 144 && !lineStart.equals(transmuterPos))
				addLine(lineStart, transmuterPos, AuraType.TRANSMUTATION);
			else if(mousePos.distanceToSqr(emitterPos) <= 144 && !lineStart.equals(emitterPos))
				addLine(lineStart, emitterPos, AuraType.EMISSION);
			else if(mousePos.distanceToSqr(conjurerPos) <= 144 && !lineStart.equals(conjurerPos))
				addLine(lineStart, conjurerPos, AuraType.CONJURATION);
			else if(mousePos.distanceToSqr(manipulatorPos) <= 144 && !lineStart.equals(manipulatorPos))
				addLine(lineStart, manipulatorPos, AuraType.MANIPULATION);
			else if(mousePos.distanceToSqr(specialistPos) <= 144 && !lineStart.equals(specialistPos))
				addLine(lineStart, specialistPos, AuraType.SPECIALIZATION);

			lineStart = null;
			Network.getNetworkHandler().sendToServer(new ServerboundSaveScrollDataPacket(menu.containerId, auraTypes));
		}

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		super.mouseMoved(mouseX, mouseY);
		mousePos = new Vec2((float) (mouseX - leftPos), (float) (mouseY - topPos));
	}

	@Override
	public void onClose() {
		Network.getNetworkHandler().sendToServer(new ServerboundSaveScrollDataPacket(menu.containerId, auraTypes));
		super.onClose();
	}

	public void setScroll(ItemStack stack) {
		this.stack = stack;
	}

	private void takeScrollButtonShit(Button buttonWidget) {
		if(Minecraft.getInstance().gameMode != null) {
			Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, 0);
			onClose();
		}
	}

	private void drawLine(PoseStack matrices, float x1, float y1, float x2, float y2) {
		DataComponentType<Boolean> completedData = DevotionData.SCROLL_COMPLETED.get();

		Color color = stack.getOrDefault(completedData, false) ? colorModulation(0.224f, 0.196f, 0.175f) : Color.fromFloatArray(Color.Ordering.RGB, 0.224f, 0.196f, 0.175f);
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(color.redF(), color.greenF(), color.blueF(), 1f);
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix = matrices.last().pose();
		float angle = (float) (Math.atan2(y2 - y1, x2 - x1) - (Math.PI * 0.5));
		float dx = Mth.cos(angle) * 2;
		float dy = Mth.sin(angle) * 2;

		bufferBuilder.addVertex(matrix, x2 - dx, y2 - dy, 0).setColor(0);
		bufferBuilder.addVertex(matrix, x2 + dx, y2 + dy, 0).setColor(0);
		bufferBuilder.addVertex(matrix, x1 + dx, y1 + dy, 0).setColor(0);
		bufferBuilder.addVertex(matrix, x1 - dx, y1 - dy, 0).setColor(0);
		MeshData data = bufferBuilder.build();

		if(data != null)
			BufferUploader.drawWithShader(data);
	}

	public Color colorModulation(float r, float g, float b) {
		DataComponentType<Long> completedTimeData = DevotionData.SCROLL_COMPLETED_TIME.get();
		float colorModifier = 0f;

		if(Minecraft.getInstance().level != null)
			colorModifier = 0.25f + (float) Math.sin((Minecraft.getInstance().level.getGameTime() - stack.getOrDefault(completedTimeData, 0).longValue()) * 0.15f) * 0.25f;

		return Color.fromFloatArray(Color.Ordering.RGB, r + colorModifier, g + colorModifier, b + colorModifier);
	}

	public void redrawLines() {
		DataComponentType<List<Integer>> undoBufferData = DevotionData.UNDO_BUFFER.get();
		List<Integer> undoBuffer = stack.getOrDefault(undoBufferData, List.of());

		if(Minecraft.getInstance().level != null) {
			lines.clear();
			auraTypes.clear();
			lineStart = null;

			if(!undoBuffer.isEmpty()) {
				for(int i = 0; i < undoBuffer.size() - 1; i++) {
					AuraType current = AuraType.values()[undoBuffer.get(i)];
					AuraType next = AuraType.values()[undoBuffer.get(i + 1)];
					Vec2 lineX = switch(current) {
						case ENHANCEMENT -> enhancerPos;
						case TRANSMUTATION -> transmuterPos;
						case EMISSION -> emitterPos;
						case CONJURATION -> conjurerPos;
						case MANIPULATION -> manipulatorPos;
						case SPECIALIZATION -> specialistPos;
						case NONE -> new Vec2(0, 0);
					};
					Vec2 lineY = switch(next) {
						case ENHANCEMENT -> enhancerPos;
						case TRANSMUTATION -> transmuterPos;
						case EMISSION -> emitterPos;
						case CONJURATION -> conjurerPos;
						case MANIPULATION -> manipulatorPos;
						case SPECIALIZATION -> specialistPos;
						case NONE -> new Vec2(0, 0);
					};

					lines.add(new Pair<>(lineX, lineY));
					auraTypes.add(current);

					if(i == undoBuffer.size() - 2)
						auraTypes.add(next);
				}
			}
		}
	}

	public void addLine(Vec2 pos1, Vec2 pos2, AuraType type) {
		for(Pair<Vec2, Vec2> line : lines)
			if((line.getFirst().equals(pos1) && line.getSecond().equals(pos2)) || (line.getFirst().equals(pos2) && line.getSecond().equals(pos1)))
				return;

		lines.add(new Pair<>(pos1, pos2));
		auraTypes.add(type);
	}
}
