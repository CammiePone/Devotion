package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.recipes.DevotionAltarRecipe;
import dev.cammiescorner.devotion.common.recipes.DevotionStaffRecipe;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class DevotionRecipes {
	public static final RegistryHandler<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryHandler.create(Registries.RECIPE_SERIALIZER, Devotion.MOD_ID);
	public static final RegistryHandler<RecipeType<?>> RECIPE_TYPES = RegistryHandler.create(Registries.RECIPE_TYPE, Devotion.MOD_ID);

	public static final RegistrySupplier<RecipeSerializer<DevotionAltarRecipe>> ALTAR_SERIALIZER = RECIPE_SERIALIZERS.register("altar_recipe", DevotionAltarRecipe.Serializer::new);
	public static final RegistrySupplier<RecipeType<DevotionAltarRecipe>> ALTAR_TYPE = RECIPE_TYPES.register("altar_recipe_type", () -> new RecipeType<>() {
		@Override
		public String toString() {
			return "altar_type";
		}
	});

	public static final RegistrySupplier<RecipeSerializer<DevotionStaffRecipe>> STAFF_SERIALIZER = RECIPE_SERIALIZERS.register("staff_recipe", () -> new SimpleCraftingRecipeSerializer<>(DevotionStaffRecipe::new));
}
