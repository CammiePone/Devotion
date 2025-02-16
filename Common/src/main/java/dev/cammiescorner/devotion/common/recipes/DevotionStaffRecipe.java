package dev.cammiescorner.devotion.common.recipes;

import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.cammiescorner.devotion.common.registries.DevotionRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Map;

public class DevotionStaffRecipe extends CustomRecipe {
	private static final ShapedRecipePattern PATTERN = ShapedRecipePattern.of(
		Map.of(
			'I', Ingredient.of(DevotionItems.STAFF_CORE.get()),
			'C', Ingredient.of(DevotionItems.STAFF_CAP.get())
		),
		"C",
		"I",
		"C"
	);

	public DevotionStaffRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		return PATTERN.matches(input);
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack result = new ItemStack(DevotionItems.STAFF.get());
		ItemStack core = input.items().stream().filter(stack -> stack.is(DevotionItems.STAFF_CORE.get())).findFirst().get();
		ItemStack cap = input.items().stream().filter(stack -> stack.is(DevotionItems.STAFF_CAP.get())).findFirst().get();

		result.set(DevotionData.STAFF_CORE.get(), core.get(DevotionData.STAFF_CORE.get()));
		result.set(DevotionData.STAFF_CAP.get(), cap.get(DevotionData.STAFF_CAP.get()));

		return result;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= PATTERN.width() && height >= PATTERN.height();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DevotionRecipes.STAFF_SERIALIZER.get();
	}
}
