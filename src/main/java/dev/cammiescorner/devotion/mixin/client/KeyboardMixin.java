package dev.cammiescorner.devotion.mixin.client;

import com.mojang.blaze3d.platform.InputUtil;
import dev.cammiescorner.devotion.api.events.client.KeyBindingCallback;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "onKey", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/option/KeyBind;setKeyPressed(Lcom/mojang/blaze3d/platform/InputUtil$Key;Z)V",
			ordinal = 0
	))
	public void devotion$onUnpressEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
		KeyBindingCallback.UNPRESSED.invoker().unpress(InputUtil.fromKeyCode(key, scancode), modifiers);
	}
}
