package dev.cammiescorner.devotion.common.data;

import com.google.gson.*;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.recipes.AmethystAltarRecipe;
import dev.cammiescorner.devotion.common.registry.DevotionRecipes;
import net.minecraft.item.Item;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.*;
import java.util.stream.Collectors;

public class ResearchReloadListener extends JsonDataLoader implements IdentifiableResourceReloader {
	public static final Identifier ID = Devotion.id("research");
	private static final Gson GSON = new GsonBuilder().create();

	public ResearchReloadListener() {
		super(GSON, "devotion_research");
	}

	public static void verifyResearchInRecipes(MinecraftServer server) {
		for(AmethystAltarRecipe altarRecipe : server.getRecipeManager().listAllOfType(DevotionRecipes.ALTAR_TYPE))
			for(Identifier researchId : altarRecipe.getRequiredResearch())
				if(Research.getById(researchId) == null)
					Devotion.LOGGER.error("Altar recipe {} has non-existent research requirement {}", altarRecipe.getId(), researchId);
	}

	@Override
	public Identifier getQuiltId() {
		return ID;
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		Devotion.RESEARCH.clear();
		Map<Research, Set<Identifier>> parentMap = new HashMap<>();

		prepared.forEach((identifier, jsonElement) -> {
			JsonObject json = JsonHelper.asObject(jsonElement, "devotion:research");
			JsonArray parents = JsonHelper.getArray(json, "parents", new JsonArray());
			Set<Identifier> parentIds = new HashSet<>();
			boolean hidden = JsonHelper.getBoolean(json, "hidden", false);
			Item item = JsonHelper.getItem(json, "item_icon");
			Research research = new Research(identifier, hidden, item);

			for(JsonElement parent : parents)
				parentIds.add(new Identifier(JsonHelper.asString(parent, "research_id")));

			parentMap.put(research, parentIds);

			Devotion.RESEARCH.put(identifier, research);
		});

		parentMap.forEach((research, identifiers) -> {
			research.setParents(identifiers.stream().map(identifier -> {
				if(!Devotion.RESEARCH.containsKey(identifier)) {
					Devotion.LOGGER.warn("Research {} is missing a parent {}", research.getId(), identifier);
					return null;
				}
				else {
					return Devotion.RESEARCH.get(identifier);
				}
			}).filter(Objects::nonNull).collect(Collectors.toSet()));
		});
	}
}
