package dev.cammiescorner.devotion.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.models.entity.ScrollModel;
import dev.cammiescorner.devotion.common.items.ResearchScrollItem;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LecternBlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LecternBlockEntityRenderer.class)
public class LecternBlockEntityRendererMixin {
	private ScrollModel scroll;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void devotion$init(BlockEntityRendererFactory.Context context, CallbackInfo info) {
		scroll = new ScrollModel(context.getLayerModelPart(ScrollModel.MODEL_LAYER));
	}

	@Inject(method = "render(Lnet/minecraft/block/entity/LecternBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/model/BookModel;renderBook(Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
	), cancellable = true)
	public void devotion$renderScroll(LecternBlockEntity lecternBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo info) {
		if(lecternBlockEntity.getBook().getItem() instanceof ResearchScrollItem) {
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
			VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumerProvider, RenderLayer.getEntitySolid(Devotion.id("textures/entity/lectern_research_scroll.png")), false, false);
			scroll.render(matrixStack, vertexConsumer, i, j, 1F, 1F, 1F, 1F);
			matrixStack.pop();
			info.cancel();
		}
	}
}
