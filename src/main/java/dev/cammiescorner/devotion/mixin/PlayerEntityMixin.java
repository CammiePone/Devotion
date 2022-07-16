package dev.cammiescorner.devotion.mixin;

import dev.cammiescorner.devotion.api.entity.DevotionAttributes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

	@Inject(method = "createPlayerAttributes", at = @At("RETURN"))
	private static void devotion$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
		info.getReturnValue().add(DevotionAttributes.AURA_COST).add(DevotionAttributes.AURA_REGEN)
				.add(DevotionAttributes.AURA_LOCK).add(DevotionAttributes.ENHANCEMENT_AFFINITY)
				.add(DevotionAttributes.TRANSMUTATION_AFFINITY).add(DevotionAttributes.EMISSION_AFFINITY)
				.add(DevotionAttributes.CONJURATION_AFFINITY).add(DevotionAttributes.MANIPULATION_AFFINITY);
	}
}
