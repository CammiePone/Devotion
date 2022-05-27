package dev.cammiescorner.arcanus.client.renderer.equipment;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.models.equipment.MageRobesModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class MageRobesRenderer implements ArmorRenderer {
	private final MinecraftClient client = MinecraftClient.getInstance();
	private final Identifier texture;
	private MageRobesModel<LivingEntity> model;

	public MageRobesRenderer(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> entityModel) {
		if(model == null)
			model = new MageRobesModel<>(client.getEntityModelLoader().getModelPart(MageRobesModel.MODEL_LAYER));

		NbtCompound nbt = stack.getSubNbt(Arcanus.MOD_ID);
		boolean isClosed = nbt != null && nbt.getBoolean("Closed");
		entityModel.setAttributes(model);
		model.setVisible(true);
		model.closedHood.visible = slot == EquipmentSlot.HEAD && isClosed;
		model.openHood.visible = slot == EquipmentSlot.HEAD && !isClosed;
		model.cloak.visible = slot == EquipmentSlot.HEAD;
		model.garb.visible = slot == EquipmentSlot.CHEST;
		model.leftSleeve.visible = slot == EquipmentSlot.CHEST;
		model.rightSleeve.visible = slot == EquipmentSlot.CHEST;
		model.belt.visible = slot == EquipmentSlot.LEGS;
		model.leftShoe.visible = slot == EquipmentSlot.FEET;
		model.rightShoe.visible = slot == EquipmentSlot.FEET;

		ModelPart cape = model.cloak.getChild("cube_r2");
		// TODO animate cape on the cloak

		ArmorRenderer.renderPart(matrices, vertices, light, stack, model, texture);
	}
}
