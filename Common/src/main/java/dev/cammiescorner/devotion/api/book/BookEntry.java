package dev.cammiescorner.devotion.api.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.research.Research;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;

public record BookEntry(Holder<BookTab> tab, Holder<Research> research, int x, int y, Set<BookPage> pages) {
	public static final Codec<Holder<BookEntry>> CODEC = RegistryFixedCodec.create(DevotionRegistries.BOOK_ENTRY);
	public static final Codec<BookEntry> DIRECT_CODEC = RecordCodecBuilder.create(entryInstance -> entryInstance.group(
		BookTab.CODEC.fieldOf("tab").forGetter(BookEntry::tab),
		Research.CODEC.fieldOf("research").forGetter(BookEntry::research),
		Codec.INT.optionalFieldOf("posX", 0).forGetter(BookEntry::x),
		Codec.INT.optionalFieldOf("posY", 0).forGetter(BookEntry::y),
		BookPage.DIRECT_CODEC.listOf().xmap(Set::copyOf, List::copyOf).optionalFieldOf("pages", Set.of()).forGetter(BookEntry::pages)
	).apply(entryInstance, BookEntry::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BookEntry>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.BOOK_ENTRY);

	public static BookEntry get(ResourceLocation id, RegistryAccess access) {
		return access.registry(DevotionRegistries.BOOK_ENTRY).orElseThrow().get(id);
	}

	public ResourceLocation getId(RegistryAccess access) {
		return access.registry(DevotionRegistries.BOOK_ENTRY).orElseThrow().getKey(this);
	}
}
