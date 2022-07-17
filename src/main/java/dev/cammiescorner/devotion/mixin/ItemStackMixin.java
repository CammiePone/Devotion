package dev.cammiescorner.devotion.mixin;

import com.google.common.collect.Multimap;
import dev.cammiescorner.devotion.api.entity.DevotionAttributes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "getTooltip", at = @At(value = "INVOKE_ASSIGN",
			target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
			ordinal = 8
	), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void devotion$negativeAuraRegen(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list, MutableText mutableText, int i, EquipmentSlot[] var6, int var7, int var8, EquipmentSlot equipmentSlot, Multimap<EntityAttribute, EntityAttributeModifier> multimap, Iterator<Map.Entry<EntityAttribute, EntityAttributeModifier>> iter, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier modifier, double d, double e) {
		EntityAttribute attribute = entry.getKey();

		if(attribute == DevotionAttributes.AURA_REGEN) {
			list.remove(list.size() - 1);
			list.add(Text.translatable(
					"attribute.modifier.plus." + modifier.getOperation().getId(),
					MODIFIER_FORMAT.format(e),
					Text.translatable((attribute).getTranslationKey())
			).formatted(Formatting.RED));
		}
	}

	@Inject(method = "getTooltip", at = @At(value = "INVOKE_ASSIGN",
			target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
			ordinal = 9
	), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void devotion$positiveAuraRegen(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list, MutableText mutableText, int i, EquipmentSlot[] var6, int var7, int var8, EquipmentSlot equipmentSlot, Multimap<EntityAttribute, EntityAttributeModifier> multimap, Iterator<Map.Entry<EntityAttribute, EntityAttributeModifier>> iter, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier modifier, double d, double e) {
		EntityAttribute attribute = entry.getKey();

		if(attribute == DevotionAttributes.AURA_REGEN) {
			list.remove(list.size() - 1);
			list.add(Text.translatable(
					"attribute.modifier.take." + modifier.getOperation().getId(),
					MODIFIER_FORMAT.format(e),
					Text.translatable((attribute).getTranslationKey())
			).formatted(Formatting.BLUE));
		}
	}
}
