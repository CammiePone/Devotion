package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MageRobesItem extends ArmorItem {
	private final AuraType primaryAuraType;
	private final List<AuraType> secondaryAuraTypes;

	public MageRobesItem(Holder<ArmorMaterial> material, Type type, Properties properties, AuraType primaryAuraType, AuraType... secondaryAuraTypes) {
		super(material, type, properties.stacksTo(1));
		this.primaryAuraType = primaryAuraType;
		this.secondaryAuraTypes = List.of(secondaryAuraTypes);
	}

	public MageRobesItem(Type type, Properties properties, AuraType primaryAuraType, AuraType... secondaryAuraTypes) {
		this(DevotionMaterials.MAGE_ROBE_MATERIAL.holder(), type, properties, primaryAuraType, secondaryAuraTypes);
	}

	public MageRobesItem(Type type, Properties properties) {
		this(type, properties, AuraType.NONE);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

		if(stack.has(DevotionData.CLOSED_HOOD.get())) {
			boolean hoodClosed = stack.get(DevotionData.CLOSED_HOOD.get());
			MutableComponent component = hoodClosed ? Devotion.translate("tooltip", "hood_up") : Devotion.translate("tooltip", "hood_down");

			tooltipComponents.add(component.withStyle(ChatFormatting.GRAY));
		}
	}

	public AuraType getPrimaryAuraType() {
		return primaryAuraType;
	}

	public List<AuraType> getSecondaryAuraTypes() {
		return secondaryAuraTypes;
	}
}
