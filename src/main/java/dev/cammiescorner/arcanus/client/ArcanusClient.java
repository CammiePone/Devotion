package dev.cammiescorner.arcanus.client;

import com.williambl.early_features.api.LivingEntityEarlyFeatureRendererRegistrationCallback;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.models.equipment.MageRobesModel;
import dev.cammiescorner.arcanus.client.models.equipment.TimeCultLeaderRobesModel;
import dev.cammiescorner.arcanus.client.models.equipment.TimeCultRobesModel;
import dev.cammiescorner.arcanus.client.renderer.blocks.AmethystAltarBlockEntityRenderer;
import dev.cammiescorner.arcanus.client.renderer.entities.AuraFeatureRenderer;
import dev.cammiescorner.arcanus.client.renderer.equipment.MageRobesRenderer;
import dev.cammiescorner.arcanus.client.renderer.equipment.TimeCultRobesRenderer;
import dev.cammiescorner.arcanus.common.registry.ArcanusBlockEntities;
import dev.cammiescorner.arcanus.common.registry.ArcanusItems;
import dev.cammiescorner.arcanus.common.registry.ArcanusKeyBinds;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ArcanusClient implements ClientModInitializer {
	public static final Identifier MAGE_ROBES = Arcanus.id("textures/entity/armor/mage_robes.png");
	public static final Identifier ENHANCER_MAGE_ROBES = Arcanus.id("textures/entity/armor/enhancer_mage_robes.png");
	public static final Identifier TRANSMUTER_MAGE_ROBES = Arcanus.id("textures/entity/armor/transmuter_mage_robes.png");
	public static final Identifier EMITTER_MAGE_ROBES = Arcanus.id("textures/entity/armor/emitter_mage_robes.png");
	public static final Identifier CONJURER_MAGE_ROBES = Arcanus.id("textures/entity/armor/conjurer_mage_robes.png");
	public static final Identifier MANIPULATOR_MAGE_ROBES = Arcanus.id("textures/entity/armor/manipulator_mage_robes.png");
	public static long clientTick = 0;

	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockEntityRendererRegistry.register(ArcanusBlockEntities.AMETHYST_ALTAR, AmethystAltarBlockEntityRenderer::new);
		ArcanusKeyBinds.register();
		ClientEvents.events();

		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		EntityModelLayerRegistry.registerModelLayer(MageRobesModel.MODEL_LAYER, MageRobesModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TimeCultRobesModel.MODEL_LAYER, TimeCultRobesModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TimeCultLeaderRobesModel.MODEL_LAYER, TimeCultLeaderRobesModel::getTexturedModelData);
		ArmorRenderer.register(new MageRobesRenderer(MAGE_ROBES), ArcanusItems.MAGE_HOOD, ArcanusItems.MAGE_ROBE, ArcanusItems.MAGE_BELT, ArcanusItems.MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(ENHANCER_MAGE_ROBES), ArcanusItems.ENHANCER_MAGE_HOOD, ArcanusItems.ENHANCER_MAGE_ROBE, ArcanusItems.ENHANCER_MAGE_BELT, ArcanusItems.ENHANCER_MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(TRANSMUTER_MAGE_ROBES), ArcanusItems.TRANSMUTER_MAGE_HOOD, ArcanusItems.TRANSMUTER_MAGE_ROBE, ArcanusItems.TRANSMUTER_MAGE_BELT, ArcanusItems.TRANSMUTER_MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(EMITTER_MAGE_ROBES), ArcanusItems.EMITTER_MAGE_HOOD, ArcanusItems.EMITTER_MAGE_ROBE, ArcanusItems.EMITTER_MAGE_BELT, ArcanusItems.EMITTER_MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(CONJURER_MAGE_ROBES), ArcanusItems.CONJURER_MAGE_HOOD, ArcanusItems.CONJURER_MAGE_ROBE, ArcanusItems.CONJURER_MAGE_BELT, ArcanusItems.CONJURER_MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(MANIPULATOR_MAGE_ROBES), ArcanusItems.MANIPULATOR_MAGE_HOOD, ArcanusItems.MANIPULATOR_MAGE_ROBE, ArcanusItems.MANIPULATOR_MAGE_BELT, ArcanusItems.MANIPULATOR_MAGE_BOOTS);
		ArmorRenderer.register(new TimeCultRobesRenderer(false), ArcanusItems.TIME_CULTIST_HOOD, ArcanusItems.TIME_CULTIST_ROBE, ArcanusItems.TIME_CULTIST_LEGGINGS, ArcanusItems.TIME_CULTIST_BOOTS);
		ArmorRenderer.register(new TimeCultRobesRenderer(true), ArcanusItems.TIME_CULTIST_LEADER_HOOD, ArcanusItems.TIME_CULTIST_LEADER_ROBE, ArcanusItems.TIME_CULTIST_LEADER_LEGGINGS, ArcanusItems.TIME_CULTIST_LEADER_BOOTS);

		LivingEntityEarlyFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, context) -> {
			if(entityRenderer instanceof PlayerEntityRenderer playerRenderer)
				entityRenderer.addEarlyFeature(new AuraFeatureRenderer<>(playerRenderer, new HeldItemFeatureRenderer<>(playerRenderer)));
		});

		for(Item hoodItem : Arcanus.HOOD_ITEMS) {
			ModelPredicateProviderRegistry.register(hoodItem, Arcanus.id("closed"), (stack, world, entity, i) -> {
				NbtCompound tag = stack.getSubNbt(Arcanus.MOD_ID);

				if(tag == null)
					return 0F;

				return tag.getBoolean("Closed") ? 1F : 0F;
			});
		}
	}
}
