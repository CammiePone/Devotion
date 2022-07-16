package dev.cammiescorner.devotion.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.items.RobesItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	@ModifyExpressionValue(method = "setModelPose", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isPartVisible(Lnet/minecraft/client/render/entity/PlayerModelPart;)Z",
			ordinal = 0
	))
	private boolean devotion$hideHat(boolean original, AbstractClientPlayerEntity player) {
		ItemStack stack = player.getEquippedStack(EquipmentSlot.HEAD);
		NbtCompound nbt = stack.getSubNbt(Devotion.MOD_ID);
		boolean isClosed = nbt != null && nbt.getBoolean("Closed");

		if(Devotion.HOOD_ITEMS.contains(stack.getItem()) && isClosed)
			return false;
		if(stack.getItem() instanceof RobesItem robes && !Devotion.HOOD_ITEMS.contains(robes))
			return false;

		return original;
	}

	@ModifyExpressionValue(method = "setModelPose", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isPartVisible(Lnet/minecraft/client/render/entity/PlayerModelPart;)Z",
			ordinal = 2
	))
	private boolean devotion$hideLeftPants(boolean original, AbstractClientPlayerEntity player) {
		if(player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof RobesItem)
			return false;

		return original;
	}

	@ModifyExpressionValue(method = "setModelPose", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isPartVisible(Lnet/minecraft/client/render/entity/PlayerModelPart;)Z",
			ordinal = 3
	))
	private boolean devotion$hideRightPants(boolean original, AbstractClientPlayerEntity player) {
		if(player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof RobesItem)
			return false;

		return original;
	}
}
