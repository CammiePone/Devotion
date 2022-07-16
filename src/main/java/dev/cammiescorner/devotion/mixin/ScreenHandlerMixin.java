package dev.cammiescorner.devotion.mixin;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
	@Inject(method = "internalOnSlotClick", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/player/PlayerEntity;onPickupSlotClick(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/ClickType;)V"
	), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private void devotion$rightClickMageHood(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo info, ClickType clickType, Slot slot, ItemStack slotStack, ItemStack cursorStack) {
		if(clickType == ClickType.RIGHT && cursorStack.isEmpty() && Devotion.HOOD_ITEMS.contains(slotStack.getItem())) {
			NbtCompound tag = slotStack.getOrCreateSubNbt(Devotion.MOD_ID);
			tag.putBoolean("Closed", !tag.getBoolean("Closed"));
			info.cancel();
		}
	}
}
