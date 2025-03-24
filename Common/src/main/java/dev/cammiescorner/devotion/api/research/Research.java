package dev.cammiescorner.devotion.api.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.api.book.BookPage;
import dev.cammiescorner.devotion.api.book.BookTab;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Research(ItemStack icon, Research.Difficulty difficulty, boolean isHidden, boolean knownByDefault, Set<ResourceLocation> parentIds, Holder<BookTab> tab, int x, int y, List<BookPage> pages) {
	public static final Codec<Holder<Research>> CODEC = RegistryFixedCodec.create(DevotionRegistries.RESEARCH);
	public static final Codec<Research> DIRECT_CODEC = RecordCodecBuilder.create(researchInstance -> researchInstance.group(
		ItemStack.CODEC.fieldOf("item_icon").forGetter(Research::icon),
		Difficulty.CODEC.fieldOf("difficulty").forGetter(Research::difficulty),
		Codec.BOOL.optionalFieldOf("hidden", false).forGetter(Research::isHidden),
		Codec.BOOL.optionalFieldOf("known_by_default", false).forGetter(Research::knownByDefault),
		ResourceLocation.CODEC.listOf().xmap(java.util.Set::copyOf, List::copyOf).optionalFieldOf("parents", java.util.Set.of()).forGetter(Research::parentIds),
		BookTab.CODEC.fieldOf("tab").forGetter(Research::tab),
		Codec.INT.optionalFieldOf("posX", 0).forGetter(Research::x),
		Codec.INT.optionalFieldOf("posY", 0).forGetter(Research::y),
		BookPage.CODEC.listOf().optionalFieldOf("pages", List.of()).forGetter(Research::pages)
	).apply(researchInstance, Research::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Research>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.RESEARCH);

	public static Research get(ResourceLocation id, RegistryAccess access) {
		return access.registry(DevotionRegistries.RESEARCH).orElseThrow().get(id);
	}

	public ResourceLocation getId(RegistryAccess access) {
		return access.registry(DevotionRegistries.RESEARCH).orElseThrow().getKey(this);
	}

	public Set<Research> getParents(HolderLookup.Provider provider) {
		HolderLookup.RegistryLookup<Research> lookup = provider.lookupOrThrow(DevotionRegistries.RESEARCH);
		return parentIds.stream().map(id -> ResourceKey.create(DevotionRegistries.RESEARCH, id)).map(lookup::getOrThrow).map(Holder.Reference::value).collect(Collectors.toSet());
	}

	public enum Difficulty implements StringRepresentable {
		EASY("easy", 4), NORMAL("normal", 6), HARD("hard", 8);

		public static final Codec<Difficulty> CODEC = StringRepresentable.fromEnum(Difficulty::values);
		private final String name;
		private final int riddleCount;

		Difficulty(String name, int riddleCount) {
			this.name = name;
			this.riddleCount = riddleCount;
		}

		@Override
		public String getSerializedName() {
			return name;
		}

		public int getRiddleCount() {
			return riddleCount;
		}
	}
}
