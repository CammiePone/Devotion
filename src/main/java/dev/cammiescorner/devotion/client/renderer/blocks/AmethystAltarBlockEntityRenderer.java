package dev.cammiescorner.devotion.client.renderer.blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.client.AuraVertexConsumerProvider;
import dev.cammiescorner.devotion.client.models.TranslucentBakedModel;
import dev.cammiescorner.devotion.common.blocks.entities.AmethystAltarBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class AmethystAltarBlockEntityRenderer implements BlockEntityRenderer<AmethystAltarBlockEntity> {
	private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
	private final BlockRenderManager blockRenderer = MinecraftClient.getInstance().getBlockRenderManager();
	private final RandomGenerator random = RandomGenerator.createLegacy(42L);

	public AmethystAltarBlockEntityRenderer(BlockEntityRendererFactory.Context cxt) {

	}

	@Override
	public void render(AmethystAltarBlockEntity altar, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = altar.getWorld();
		int filledSlots = altar.filledSlots();

		if(world != null && !altar.isSchematicHidden()) {
			double time = DevotionClient.clientTick + tickDelta;
			HashMap<BlockPos, BlockState> structureMap = DevotionHelper.getStructureMap(world);
			BlockPos altarOffset = DevotionHelper.getAltarOffset(world);

			if(structureMap != null && !structureMap.isEmpty()) {
				float scale = (float) (0.8125F + (Math.sin(time * 0.075) * 0.0625F));

				for(Map.Entry<BlockPos, BlockState> entry : structureMap.entrySet()) {
					BlockPos pos = entry.getKey();
					BlockPos adjustedPos = altar.getPos().add(pos).add(altarOffset);
					BlockState state = entry.getValue();
					BlockState realState = world.getBlockState(adjustedPos);

					if(state.getProperties().contains(Properties.WATERLOGGED))
						state = state.with(Properties.WATERLOGGED, false);
					if(realState.getProperties().contains(Properties.WATERLOGGED))
						realState = realState.with(Properties.WATERLOGGED, false);

					matrices.push();

					if(!altar.isCompleted() && !realState.equals(state)) {
						VertexConsumer vertices;
						BakedModel model;

						matrices.translate(pos.getX() + altarOffset.getX(), pos.getY(), pos.getZ() + altarOffset.getZ());

						if(world.isAir(adjustedPos)) {
							matrices.translate(0.5, 0.5, 0.5);
							matrices.scale(scale, scale, scale);
							matrices.translate(-0.5, -0.5, -0.5);
							vertices = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
							model = TranslucentBakedModel.wrap(blockRenderer.getModel(state), () -> 0.5F);
						}
						else {
							matrices.translate(0.5, 0.5, 0.5);
							matrices.scale(1.001F, 1.001F, 1.001F);
							matrices.translate(-0.5, -0.5, -0.5);

							vertices = vertexConsumers.getBuffer(RenderLayer.getLightning());
							RenderSystem.setShaderColor(1F, 0F, 0F, 1F);
							model = blockRenderer.getModel(Blocks.STONE.getDefaultState());
						}

						long seed = state.getRenderingSeed(altar.getPos());

						blockRenderer.getModelRenderer().render(world, model, state, altar.getPos(), matrices, vertices, false, random, seed, overlay);
					}

					matrices.pop();
				}
			}

			if(filledSlots > 0) {
				double radius = 1.25;
				double angleBetween = 360 / (double) filledSlots;

				matrices.push();
				matrices.translate(0.5, 1, 0.5);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (time * 2F)));

				if(altar.isCrafting() && altar.getPower() >= altar.getRequiredPower()) {
					float timer = altar.getCraftingTime() + tickDelta;
					matrices.translate(0, 0.01 * Math.min(100, timer), 0);

					if(altar.getCraftingTime() >= 100)
						radius -= (Math.min(20, timer - 100) * 0.0625);
				}

				for(int i = 0; i < filledSlots; ++i) {
					ItemStack stack = altar.getStack(i);
					double angle = Math.toRadians(angleBetween * i);
					double rotX = Math.cos(angle) * radius;
					double rotZ = Math.sin(angle) * radius;

					matrices.push();
					matrices.translate(rotX, 0, rotZ);
					matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(90));
					matrices.multiply(Vec3f.NEGATIVE_Y.getRadialQuaternion((float) angle));

					itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, new AuraVertexConsumerProvider(vertexConsumers, 255, 255, 255, 255), (int) altar.getPos().asLong());
					itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, (int) altar.getPos().asLong());
					matrices.pop();
				}

				matrices.pop();
			}
		}
	}
}
