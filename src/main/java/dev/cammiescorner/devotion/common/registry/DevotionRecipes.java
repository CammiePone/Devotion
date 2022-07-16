package dev.cammiescorner.devotion.common.registry;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.recipes.AmethystAltarRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public class DevotionRecipes {
	public static final RecipeType<AmethystAltarRecipe> ALTAR_TYPE = RecipeType.register(Devotion.id("amethyst_altar").toString());
	public static final RecipeSerializer<AmethystAltarRecipe> ALTAR_SERIALIZER = RecipeSerializer.register(Devotion.id("amethyst_altar").toString(), new AmethystAltarRecipe.Serializer());

	public static void loadMeBitch() { }
}
