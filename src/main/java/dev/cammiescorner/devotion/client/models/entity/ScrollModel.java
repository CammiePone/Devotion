package dev.cammiescorner.devotion.client.models.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.Devotion;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ScrollModel extends EntityModel<Entity> {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Devotion.id("lectern_research_scroll"), "main");
	public final ModelPart root;
	public final ModelPart page;
	public final ModelPart leftFurl;
	public final ModelPart rightFurl;

	public ScrollModel(ModelPart modelPart) {
		this.root = modelPart;
		this.page = root.getChild("page");
		this.leftFurl = root.getChild("leftFurl");
		this.rightFurl = root.getChild("rightFurl");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();

		root.addChild("page", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, 0.0F, -4.0F, 10.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		root.addChild("leftFurl", ModelPartBuilder.create().uv(0, 8).cuboid(-1.0F, -2.0F, -4.0F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.0F, 0.0F, 0.0F));
		root.addChild("rightFurl", ModelPartBuilder.create().uv(0, 8).mirrored().cuboid(-1.0F, -2.0F, -4.0F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(6.0F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
