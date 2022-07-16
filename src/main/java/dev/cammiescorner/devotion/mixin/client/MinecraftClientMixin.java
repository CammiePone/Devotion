package dev.cammiescorner.devotion.mixin.client;

import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.common.packets.c2s.CastSpellPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow @Nullable public ClientPlayerEntity player;
	@Shadow @Final public GameRenderer gameRenderer;

	@Inject(method = "doItemUse", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
			ordinal = 1
	), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void devotion$castSpell(CallbackInfo info, Hand[] var1, int var2, int var3, Hand hand, ItemStack itemStack) {
		if(player != null) {
			Spell spell = DevotionHelper.getSelectedSpell(player);

			if(DevotionHelper.canCastSpell(player, spell)) {
				CastSpellPacket.send();

				if(spell.isInstant())
					player.swingHand(Hand.MAIN_HAND);

				gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
				info.cancel();
			}
		}
	}
}
