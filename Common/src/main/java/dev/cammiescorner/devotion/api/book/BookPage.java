package dev.cammiescorner.devotion.api.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;

public record BookPage(String text, ResourceLocation picture) {
	public static final ResourceLocation NO_PICTURE = Devotion.id("no_picture");
	public static final Codec<Holder<BookPage>> CODEC = RegistryFixedCodec.create(DevotionRegistries.BOOK_PAGE);
	public static final Codec<BookPage> DIRECT_CODEC = RecordCodecBuilder.create(pageInstance -> pageInstance.group(
		Codec.STRING.optionalFieldOf("text_key", "").forGetter(BookPage::text),
		ResourceLocation.CODEC.optionalFieldOf("picture_location", NO_PICTURE).forGetter(BookPage::picture)
	).apply(pageInstance, BookPage::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BookPage>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.BOOK_PAGE);

	@Override
	public ResourceLocation picture() {
		return picture.withPrefix("textures/devotion/book_pictures/").withSuffix(".png");
	}
}
