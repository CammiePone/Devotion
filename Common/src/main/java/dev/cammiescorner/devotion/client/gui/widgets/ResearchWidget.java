package dev.cammiescorner.devotion.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

import static dev.cammiescorner.devotion.client.DevotionClient.client;

public class ResearchWidget extends AbstractButton {
	public static final ResourceLocation TEXTURE = Devotion.id("textures/gui/scripts_of_devotion_icons.png");
	private final RegistryAccess access = client.player.registryAccess();
	private final Holder.Reference<Research> research;
	private final OnPress onPress;
	private float offsetX, offsetY;

	public ResearchWidget(int x, int y, ResourceLocation researchId, OnPress onPress) {
		super(x, y, 30, 30, Component.empty());
		this.research = access.lookupOrThrow(DevotionRegistries.RESEARCH).getOrThrow(ResourceKey.create(DevotionRegistries.RESEARCH, researchId));
		this.onPress = onPress;

		Set<ResourceLocation> playerResearch = MainHelper.getResearchIds(client.player);

		if(playerResearch.contains(research.key().location())) {
			active = true;
			visible = true;
		}
		else if(playerResearch.containsAll(research.value().parentIds())) {
			active = true;
			visible = true;
		}
		else {
			active = false;

			if(research.value().isHidden() || research.value().getParents(access).stream().anyMatch(Research::isHidden))
				visible = false;
		}
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
	public void onPress() {
		onPress.onPress(this);
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		Set<ResourceLocation> playerResearch = MainHelper.getResearchIds(client.player);

		if(research != null) {
			if(playerResearch.contains(research.key().location()))
				visible = true;
			else if(playerResearch.containsAll(research.value().parentIds()))
				visible = true;
			else if(research.value().isHidden() || research.value().getParents(access).stream().anyMatch(Research::isHidden))
				visible = false;

			if(visible) {
				isHovered = mouseX >= getX() + offsetX && mouseY >= getY() + offsetY && mouseX < getX() + offsetX + width && mouseY < getY() + offsetY + height;
				renderButton(guiGraphics, playerResearch);
			}
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
		defaultButtonNarrationText(narrationElementOutput);
	}

	public void renderButton(GuiGraphics guiGraphics, Set<ResourceLocation> playerResearch) {
		ItemStack stack = research.value().icon();
		int u;

		if(playerResearch.contains(research.key().location())) {
			u = 60;
			active = true;
		}
		else if(playerResearch.containsAll(research.value().parentIds())) {
			u = 30;
			active = true;
		}
		else {
			u = 0;
			active = false;
		}

		guiGraphics.blit(TEXTURE, getX(), getY(), u, 0, width, height);

		if(playerResearch.containsAll(research.value().parentIds()) || playerResearch.contains(research.key().location()) || research.value().parentIds().isEmpty())
			guiGraphics.renderItem(stack, getX() + 7, getY() + 7);
		else
			guiGraphics.blit(TEXTURE, getX() + 7, getY() + 7, 0, 32, 16, 16);
	}

	public void renderTooltip(GuiGraphics guiGraphics, PoseStack poseStack, int mouseX, int mouseY) {
		if(isHovered() && active && guiGraphics.containsPointInScissor(mouseX, mouseY)) {
			poseStack.pushPose();
			poseStack.translate(-offsetX, -offsetY, 0);
			guiGraphics.renderTooltip(client.font, Component.translatable(Util.makeDescriptionId("devotion_research", research.key().location())), mouseX, mouseY);
			poseStack.popPose();
		}
	}

	public void setOffset(float offsetX, float offsetY, int leftPos, int topPos) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public Holder.Reference<Research> getResearch() {
		return research;
	}

	public interface OnPress {
		void onPress(ResearchWidget widget);
	}
}
