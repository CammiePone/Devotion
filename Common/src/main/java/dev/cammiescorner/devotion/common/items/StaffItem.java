package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.client.ClientHelper;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class StaffItem extends Item {
	public StaffItem() {
		super(new Properties().stacksTo(1).component(DevotionData.STAFF_CORE.get(), DevotionStaffCores.OAK_CORE.holder()).component(DevotionData.STAFF_CAP.get(), DevotionStaffCaps.IRON_CAP.holder()));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		float discount = StaffCap.discount(stack);
		ChatFormatting formatting = discount < 0 ? ChatFormatting.RED : ChatFormatting.BLUE;

		if(discount != 0)
			tooltipComponents.add(Component.literal(String.format("%+.0f", discount * 100)).append("% ").append(Devotion.translate("tooltip", "aura_discount")).withStyle(formatting));
	}

	@Override
	public Component getName(ItemStack stack) {
		StaffCore core = stack.get(DevotionData.STAFF_CORE.get()).value();
		StaffCap cap = stack.get(DevotionData.STAFF_CAP.get()).value();
		String specificTranslate = getDescriptionId(stack) + String.format(".%s_cap.%s_core", cap.getResourceLocation().toLanguageKey(), core.getResourceLocation().toLanguageKey());

		if(SparkweaveApi.CLIENTSIDE_ENVIRONMENT && ClientHelper.hasSpecificTranslation(specificTranslate))
			return Component.translatable(specificTranslate);

		return Component.translatable(getDescriptionId(stack), Component.translatable(cap.getStaffId()), Component.translatable(core.getStaffId()));
	}
}
