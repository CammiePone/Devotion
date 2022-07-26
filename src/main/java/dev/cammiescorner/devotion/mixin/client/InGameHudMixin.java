package dev.cammiescorner.devotion.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.client.ClientEvents;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
	@Shadow protected abstract PlayerEntity getCameraPlayer();
	@Shadow private int scaledHeight;
	@Shadow private int scaledWidth;
	@Shadow @Final private static Identifier WIDGETS_TEXTURE;

	@Inject(method = "renderHotbar", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
			ordinal = 1
	))
	public void devotion$renderCastingOverlay(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		if(DevotionHelper.canUseAura(getCameraPlayer())) {
			boolean isCasting = DevotionHelper.isCasting(getCameraPlayer());
			int i = scaledWidth / 2;
			Arm arm = getCameraPlayer().getMainArm();

			if(arm == Arm.LEFT) {
				drawTexture(matrices, i - 121, scaledHeight - 23, 24, 22, 29, 24);

				RenderSystem.setShaderTexture(0, ClientEvents.HUD_ELEMENTS);
				drawTexture(matrices, i - 118, scaledHeight - 19, 128, 0, 16, 16);
				RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);

				if(isCasting)
					drawTexture(matrices, i - 122, scaledHeight - 23, 0, 22, 24, 22);
			}
			else {
				drawTexture(matrices, i + 98, scaledHeight - 23, 24, 22, 29, 24);

				RenderSystem.setShaderTexture(0, ClientEvents.HUD_ELEMENTS);
				drawTexture(matrices, i + 101, scaledHeight - 19, 128, 0, 16, 16);
				RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);

				if(isCasting)
					drawTexture(matrices, i + 97, scaledHeight - 23, 0, 22, 24, 22);
			}
		}
	}
}
