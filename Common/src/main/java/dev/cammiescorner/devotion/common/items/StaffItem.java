package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.client.ClientHelper;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionSpellFoci;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class StaffItem extends Item {
	public StaffItem() {
		super(new Properties()
			.stacksTo(1)
			.component(DevotionData.STAFF_CORE.get(), DevotionStaffCores.OAK_CORE.holder())
			.component(DevotionData.STAFF_CAP.get(), DevotionStaffCaps.IRON_CAP.holder())
			.component(DevotionData.SPELL_FOCUS.get(), DevotionSpellFoci.BLANK.holder())
			.attributes(StaffItem.constructModifiers())
		);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		player.startUsingItem(usedHand);
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
	}

	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
		AABB aabb = new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5).move(livingEntity.getEyePosition()).move(livingEntity.getLookAngle());
		ChunkAccess chunk = level.getChunk(BlockPos.containing(aabb.getCenter()));
		Map<BlockPos, AuraNode> auraNodeMap = MainHelper.getAuraNodes(chunk);

		for(BlockPos blockPos : auraNodeMap.keySet()) {
			if(aabb.contains(blockPos.getCenter())) {
				AuraNode auraNode = auraNodeMap.get(blockPos);

				System.out.println(auraNode.viewAuraMap());
			}
		}
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
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

	private static ItemAttributeModifiers constructModifiers() {
		return ItemAttributeModifiers
			.builder().add(
				Attributes.ATTACK_DAMAGE,
				new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 4, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND
			).add(
				Attributes.ATTACK_SPEED,
				new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.6, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND
			).build();
	}
}
