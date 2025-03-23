package dev.cammiescorner.devotion.api.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.Devotion;
import net.minecraft.resources.ResourceLocation;

public record BookPage(String text, ResourceLocation picture) {
	public static final ResourceLocation NO_PICTURE = Devotion.id("no_picture");
	public static final Codec<BookPage> CODEC = RecordCodecBuilder.create(pageInstance -> pageInstance.group(
		Codec.STRING.optionalFieldOf("text_key", "").forGetter(BookPage::text),
		ResourceLocation.CODEC.optionalFieldOf("picture_location", NO_PICTURE).forGetter(BookPage::picture)
	).apply(pageInstance, BookPage::new));

	@Override
	public ResourceLocation picture() {
		return picture.withPrefix("textures/devotion/book_pictures/").withSuffix(".png");
	}
}
