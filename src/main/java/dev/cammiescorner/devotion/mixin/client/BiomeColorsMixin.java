package dev.cammiescorner.devotion.mixin.client;

import dev.cammiescorner.devotion.common.components.chunk.PurpleWaterComponent;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = BiomeColors.class, priority = 9999)
public class BiomeColorsMixin {
	@Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
	private static void devotion$baterWucket(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		Optional<PurpleWaterComponent> optional = DevotionComponents.PURPLE_WATER_COMPONENT.maybeGet(MinecraftClient.getInstance().world.getChunk(pos)).filter(component -> component.isPosNearAltar(pos.toImmutable()));

		if(optional.isPresent())
			info.setReturnValue(0x8408e3);
	}
}
