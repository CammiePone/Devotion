package dev.cammiescorner.devotion.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.client.ClientHelper;
import dev.cammiescorner.devotion.client.renderers.AuraVertexBufferSource;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.upcraft.sparkweave.api.color.Color;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Shadow private ItemStack mainHandItem;
	@Shadow protected abstract void renderArmWithItem(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight);

	@Shadow
	private ItemStack offHandItem;

	@Inject(method = "renderHandsWithItems", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		ordinal = 0
	), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void renderAuraForMainHandItem(float partialTicks, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LocalPlayer player, int combinedLight, CallbackInfo ci, float f, InteractionHand interactionhand, float f1, ItemInHandRenderer.HandRenderSelection iteminhandrenderer$handrenderselection, float f2, float f3, float f4, float f5) {
		AuraType primaryAuraType = MainHelper.getPrimaryAuraType(player);
		float aura = MainHelper.getAura(player, primaryAuraType);

		if(aura > 0 && ClientHelper.shouldRenderAura(player)) {
			Color auraColor = primaryAuraType.getColor();
			float alpha = MainHelper.getAuraAlpha(player, primaryAuraType);

			renderArmWithItem(player, partialTicks, f1, InteractionHand.MAIN_HAND, f4, mainHandItem, f5, poseStack, new AuraVertexBufferSource(buffer, auraColor.red(), auraColor.green(), auraColor.blue(), (int) (alpha * 255)), combinedLight);
		}
	}

	@Inject(method = "renderHandsWithItems", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		ordinal = 1
	), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void renderAuraForOffHandItem(float partialTicks, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LocalPlayer player, int combinedLight, CallbackInfo ci, float f, InteractionHand interactionhand, float f1, ItemInHandRenderer.HandRenderSelection iteminhandrenderer$handrenderselection, float f2, float f3, float f6, float f7) {
		AuraType primaryAuraType = MainHelper.getPrimaryAuraType(player);
		float aura = MainHelper.getAura(player, primaryAuraType);

		if(aura > 0 && ClientHelper.shouldRenderAura(player)) {
			Color auraColor = primaryAuraType.getColor();
			float alpha = MainHelper.getAuraAlpha(player, primaryAuraType);

			renderArmWithItem(player, partialTicks, f1, InteractionHand.OFF_HAND, f6, offHandItem, f7, poseStack, new AuraVertexBufferSource(buffer, auraColor.red(), auraColor.green(), auraColor.blue(), (int) (alpha * 255)), combinedLight);
		}
	}
}
