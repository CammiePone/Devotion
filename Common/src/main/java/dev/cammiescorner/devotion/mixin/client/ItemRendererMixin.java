package dev.cammiescorner.devotion.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@WrapOperation(method = "getModel", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;"
	))
	private BakedModel bakeModelsForCapsAndCores(ItemModelShaper instance, ItemStack stack, Operation<BakedModel> original) {
		if(stack.is(DevotionItems.STAFF_CORE.get())) {
			StaffCore core = stack.get(DevotionData.STAFF_CORE.get()).value();
			ResourceLocation id = DevotionStaffCores.REGISTRY.getKey(core);

			return instance.getModelManager().getModel(new ModelResourceLocation(id.withPrefix("item/staff_core/"), "standalone"));
		}

		if(stack.is(DevotionItems.STAFF_CAP.get())) {
			StaffCap cap = stack.get(DevotionData.STAFF_CAP.get()).value();
			ResourceLocation id = DevotionStaffCaps.REGISTRY.getKey(cap);

			return instance.getModelManager().getModel(new ModelResourceLocation(id.withPrefix("item/staff_cap/"), "standalone"));
		}

		return original.call(instance, stack);
	}
}
