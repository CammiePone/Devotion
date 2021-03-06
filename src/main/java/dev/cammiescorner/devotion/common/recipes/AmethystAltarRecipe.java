package dev.cammiescorner.devotion.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.cammiescorner.devotion.api.actions.ConfiguredAltarAction;
import dev.cammiescorner.devotion.common.blocks.entities.AmethystAltarBlockEntity;
import dev.cammiescorner.devotion.common.registry.DevotionRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AmethystAltarRecipe implements Recipe<AmethystAltarBlockEntity> {
	private final Identifier id;
	private final String group;
	private final DefaultedList<Ingredient> input;
	private final int power;
	private final ConfiguredAltarAction result;
	private final List<Identifier> requiredResearch;

	public AmethystAltarRecipe(Identifier id, String group, DefaultedList<Ingredient> input, int power, ConfiguredAltarAction result, List<Identifier> requiredResearch) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.power = power;
		this.result = result;
		this.requiredResearch = requiredResearch;
	}

	@Override
	public boolean matches(AmethystAltarBlockEntity altar, World world) {
		RecipeMatcher matcher = new RecipeMatcher();
		int i = 0;

		for(int j = 0; j < altar.size(); ++j) {
			ItemStack stack = altar.getStack(j);

			if(stack.isEmpty())
				continue;

			++i;
			matcher.addInput(stack, 1);
		}

		return i == input.size() && matcher.match(this, null);
	}

	@Override
	public ItemStack craft(AmethystAltarBlockEntity altar) {
		return ItemStack.EMPTY;
	}

	public void craft(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar) {
		result.run(world, player, altar);
		altar.clear();
	}

	@Override
	public boolean fits(int width, int height) {
		return width <= 10 && width > 0 && height == 1;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return input;
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	public ConfiguredAltarAction getResult() {
		return result;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DevotionRecipes.ALTAR_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return DevotionRecipes.ALTAR_TYPE;
	}

	public int getPower() {
		return power;
	}

	public List<Identifier> getRequiredResearch() {
		return requiredResearch;
	}

	public static class Serializer implements RecipeSerializer<AmethystAltarRecipe> {
		@Override
		public AmethystAltarRecipe read(Identifier id, JsonObject json) {
			String group = JsonHelper.getString(json, "group", "");
			DefaultedList<Ingredient> ingredients = ShapelessRecipe.Serializer.getIngredients(JsonHelper.getArray(json, "ingredients"));
			int power = JsonHelper.getInt(json, "power");
			JsonObject resultObj = JsonHelper.getObject(json, "result");
			String result = JsonHelper.getString(resultObj, "type");
			AltarAction action = Devotion.ALTAR_ACTIONS.getOrEmpty(new Identifier(result)).orElseThrow(() -> new JsonSyntaxException("Expected result to be an altar action, was unknown string '" + result + "'"));
			ConfiguredAltarAction altarAction = action.create(resultObj);

			JsonArray array = JsonHelper.getArray(json, "research", new JsonArray());
			List<Identifier> research = new ArrayList<>();

			for(int i = 0; i < array.size(); i++)
				research.add(new Identifier(JsonHelper.asString(array.get(i), "research_" + i)));

			if(ingredients.isEmpty())
				throw new JsonParseException("No ingredients for altar recipe");
			if(ingredients.size() > 10)
				throw new JsonParseException("Too many ingredients for altar recipe");

			return new AmethystAltarRecipe(id, group, ingredients, power, altarAction, research);
		}

		@Override
		public AmethystAltarRecipe read(Identifier id, PacketByteBuf buf) {
			String group = buf.readString();
			int ingredientSize = buf.readVarInt();
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(ingredientSize, Ingredient.EMPTY);
			ingredients.replaceAll(ignored -> Ingredient.fromPacket(buf));
			int power = buf.readVarInt();
			AltarAction action = Devotion.ALTAR_ACTIONS.get(buf.readVarInt());
			ConfiguredAltarAction altarAction = action.create(buf);
			int researchSize = buf.readVarInt();
			List<Identifier> research = new ArrayList<>();

			for(int i = 0; i < researchSize; i++)
				research.add(buf.readIdentifier());

			return new AmethystAltarRecipe(id, group, ingredients, power, altarAction, research);
		}

		@Override
		public void write(PacketByteBuf buf, AmethystAltarRecipe recipe) {
			buf.writeString(recipe.getGroup());
			buf.writeVarInt(recipe.getIngredients().size());

			for(Ingredient ingredient : recipe.getIngredients())
				ingredient.write(buf);

			buf.writeVarInt(recipe.getPower());
			buf.writeVarInt(Devotion.ALTAR_ACTIONS.getRawId(recipe.getResult().getType()));
			recipe.getResult().write(buf);
			buf.writeVarInt(recipe.getRequiredResearch().size());

			for(int i = 0; i < recipe.getRequiredResearch().size(); i++)
				buf.writeIdentifier(recipe.getRequiredResearch().get(i));
		}
	}
}
