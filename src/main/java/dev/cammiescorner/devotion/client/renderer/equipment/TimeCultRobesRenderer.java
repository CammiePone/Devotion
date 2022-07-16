package dev.cammiescorner.devotion.client.renderer.equipment;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.models.equipment.TimeCultLeaderRobesModel;
import dev.cammiescorner.devotion.client.models.equipment.TimeCultRobesModel;
import dev.cammiescorner.devotion.common.registry.DevotionItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class TimeCultRobesRenderer implements ArmorRenderer {
	private final MinecraftClient client = MinecraftClient.getInstance();
	private final boolean isLeaderRobes;
	private final Identifier texture;
	private TimeCultRobesModel<LivingEntity> normalModel;
	private TimeCultLeaderRobesModel<LivingEntity> leaderModel;

	public TimeCultRobesRenderer(boolean isLeaderRobes) {
		this.isLeaderRobes = isLeaderRobes;

		if(isLeaderRobes)
			this.texture = Devotion.id("textures/entity/armor/time_cultist_leader_robes.png");
		else
			this.texture = Devotion.id("textures/entity/armor/time_cultist_robes.png");
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> entityModel) {
		if(normalModel == null)
			normalModel = new TimeCultRobesModel<>(client.getEntityModelLoader().getModelPart(TimeCultRobesModel.MODEL_LAYER));
		if(leaderModel == null)
			leaderModel = new TimeCultLeaderRobesModel<>(client.getEntityModelLoader().getModelPart(TimeCultLeaderRobesModel.MODEL_LAYER));

		if(isLeaderRobes) {
			entityModel.setAttributes(leaderModel);
			leaderModel.setVisible(true);
			leaderModel.skull.visible = slot == EquipmentSlot.HEAD;
			leaderModel.hood.visible = slot == EquipmentSlot.CHEST && entity.getEquippedStack(EquipmentSlot.HEAD).getItem() == DevotionItems.TIME_CULTIST_LEADER_HOOD;
			leaderModel.cape.visible = slot == EquipmentSlot.CHEST;
			leaderModel.robe.visible = slot == EquipmentSlot.LEGS;
			leaderModel.leftLegArmour.visible = slot == EquipmentSlot.LEGS;
			leaderModel.rightLegArmour.visible = slot == EquipmentSlot.LEGS;
			leaderModel.leftShoe.visible = slot == EquipmentSlot.FEET;
			leaderModel.rightShoe.visible = slot == EquipmentSlot.FEET;

			leaderModel.leftRobe.setAngles(entityModel.rightArm.pitch * 0.8F, 0, 0);
			leaderModel.rightRobe.setAngles(entityModel.leftArm.pitch * 0.8F, 0, 0);

			ArmorRenderer.renderPart(matrices, vertices, light, stack, leaderModel, texture);
		}
		else {
			NbtCompound nbt = stack.getSubNbt(Devotion.MOD_ID);
			boolean isClosed = nbt != null && nbt.getBoolean("Closed");
			entityModel.setAttributes(normalModel);
			normalModel.setVisible(true);
			normalModel.closedHood.visible = slot == EquipmentSlot.HEAD && isClosed;
			normalModel.openHood.visible = slot == EquipmentSlot.HEAD && !isClosed;
			normalModel.cloak.visible = slot == EquipmentSlot.HEAD;
			normalModel.garb.visible = slot == EquipmentSlot.CHEST;
			normalModel.leftSleeve.visible = slot == EquipmentSlot.CHEST;
			normalModel.rightSleeve.visible = slot == EquipmentSlot.CHEST;
			normalModel.leftLegSleeve.visible = slot == EquipmentSlot.LEGS;
			normalModel.rightLegSleeve.visible = slot == EquipmentSlot.LEGS;
			normalModel.leftShoe.visible = slot == EquipmentSlot.FEET;
			normalModel.rightShoe.visible = slot == EquipmentSlot.FEET;
			ArmorRenderer.renderPart(matrices, vertices, light, stack, normalModel, texture);
		}
	}
}
