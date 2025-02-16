package dev.cammiescorner.devotion.api.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record BookEntry(ItemStack icon, Holder<BookTab> tab, Holder<Research> research, int x, int y) {
	public static final Codec<Holder<BookEntry>> CODEC = RegistryFixedCodec.create(DevotionRegistryKeys.BOOK_ENTRY);
	public static final Codec<BookEntry> DIRECT_CODEC = RecordCodecBuilder.create(tabInstance -> tabInstance.group(
		ItemStack.CODEC.fieldOf("item_icon").forGetter(BookEntry::icon),
		BookTab.CODEC.fieldOf("tab").forGetter(BookEntry::tab),
		Research.CODEC.fieldOf("research").forGetter(BookEntry::research),
		Codec.INT.optionalFieldOf("posX", 0).forGetter(BookEntry::x),
		Codec.INT.optionalFieldOf("posY", 0).forGetter(BookEntry::y)

		// TODO add page data pain and suffering
	).apply(tabInstance, BookEntry::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BookEntry>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistryKeys.BOOK_ENTRY);

	public static BookEntry get(ResourceLocation id, RegistryAccess access) {
		return access.registry(DevotionRegistryKeys.BOOK_ENTRY).orElseThrow().get(id);
	}

	public ResourceLocation getId(RegistryAccess access) {
		return access.registry(DevotionRegistryKeys.BOOK_ENTRY).orElseThrow().getKey(this);
	}
}
