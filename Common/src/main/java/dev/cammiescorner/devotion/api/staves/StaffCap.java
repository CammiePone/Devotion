package dev.cammiescorner.devotion.api.staves;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class StaffCap {
	public static final Codec<Holder<StaffCap>> CODEC = RegistryFixedCodec.create(DevotionRegistries.STAFF_CAP);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<StaffCap>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.STAFF_CAP);
	private final float discount;
	private final boolean inert;
	private ResourceLocation resourceLocation;
	private String descriptionId;
	private String staffId;

	public StaffCap(float discount, boolean inert) {
		this.discount = discount;
		this.inert = inert;
	}

	public StaffCap(float discount) {
		this(discount, false);
	}

	public ResourceLocation getResourceLocation() {
		if(resourceLocation == null)
			resourceLocation = DevotionStaffCaps.REGISTRY.getKey(this);

		return resourceLocation;
	}

	public ResourceLocation getItemModelLocation() {
		return getResourceLocation().withPrefix("item/staff_cap/");
	}

	public ResourceLocation getStaffModelLocation() {
		return getResourceLocation().withPrefix("staff_part/staff_cap/");
	}

	public String getDescriptionId() {
		if(descriptionId == null)
			descriptionId = Util.makeDescriptionId("item", DevotionStaffCaps.REGISTRY.getKey(this));

		return descriptionId;
	}

	public String getStaffId() {
		if(staffId == null)
			staffId = Util.makeDescriptionId("staff", DevotionStaffCaps.REGISTRY.getKey(this)) + "ped";

		return staffId;
	}

	public float discount() {
		return discount;
	}

	public boolean inert() {
		return inert;
	}

	public static float discount(ItemStack stack) {
		return stack.get(DevotionData.STAFF_CAP.get()).value().discount();
	}
}
