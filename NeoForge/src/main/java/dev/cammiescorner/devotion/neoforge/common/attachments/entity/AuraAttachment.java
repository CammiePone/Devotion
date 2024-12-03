package dev.cammiescorner.devotion.neoforge.common.attachments.entity;

import commonnetwork.api.Network;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundAuraPacket;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.*;

public class AuraAttachment implements INBTSerializable<CompoundTag> {
	private static final Set<Class<? extends Entity>> AURA_PROVIDERS = new HashSet<>();
	private static final RandomSource random = RandomSource.create();
	public static final float MAX_AURA = 100;
	private final LivingEntity entity;
	private final Map<AuraType, Float> aura = new HashMap<>();
	private AuraType primaryAuraType;
	private long lastTimeAuraChanged;

	public AuraAttachment(IAttachmentHolder holder) {
		this.entity = holder instanceof LivingEntity entity ? entity : null;
		this.primaryAuraType = AuraType.NONE;
		// FIXME for whatever reason this breaks the aura shader AND changes every time the player dies
//		this.primaryAuraType = AuraType.values()[random.nextInt(AuraType.values().length)];

		for(AuraType auraType : AuraType.values())
			this.aura.put(auraType, MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType));
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		if(tag.contains("AuraMap", Tag.TAG_COMPOUND)) {
			aura.clear();

			ListTag listTag = tag.getList("AuraMap", Tag.TAG_COMPOUND);

			for(int i = 0; i < listTag.size(); i++) {
				CompoundTag compoundTag = listTag.getCompound(i);
				aura.put(AuraType.byName(compoundTag.getString("AuraType")), compoundTag.getFloat("AuraAmount"));
			}
		}

		if(tag.contains("PrimaryAuraType", Tag.TAG_STRING))
			primaryAuraType = AuraType.byName(tag.getString("PrimaryAuraType"));

		lastTimeAuraChanged = tag.getLong("LastTimeAuraChanged");
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();
		ListTag listTag = new ListTag();

		for(Map.Entry<AuraType, Float> entry : aura.entrySet()) {
			CompoundTag compoundTag = new CompoundTag();

			compoundTag.putString("AuraType", entry.getKey().getSerializedName());
			compoundTag.putFloat("AuraAmount", entry.getValue());
			listTag.add(compoundTag);
		}

		tag.put("AuraMap", listTag);
		tag.putString("PrimaryAuraType", primaryAuraType.getSerializedName());
		tag.putLong("LastTimeAuraChanged", lastTimeAuraChanged);

		return tag;
	}

	public Map<AuraType, Float> getAllAuraValues() {
		return Map.copyOf(aura);
	}

	public float getAura(AuraType auraType) {
		return aura.get(auraType);
	}

	public void setAura(AuraType auraType, float amount) {
		aura.put(auraType, Mth.clamp(amount, 0, MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType)));

		if(entity != null)
			lastTimeAuraChanged = entity.level().getGameTime();

		sync();
	}

	public long getLastTimeAuraChanged() {
		return lastTimeAuraChanged;
	}

	public AuraType getPrimaryAuraType() {
		return primaryAuraType;
	}

	public void setPrimaryAuraType(AuraType primaryAuraType) {
		this.primaryAuraType = primaryAuraType;
		sync();
	}

	public float getAuraAlpha() {
		return aura.get(primaryAuraType) / MAX_AURA;
	}

	public void sync() {
		if(entity != null && entity.level() instanceof ServerLevel level && entity.hasData(NeoMain.AURA)) {
			AuraAttachment attachment = entity.getData(NeoMain.AURA);

			Network.getNetworkHandler().sendToClientsLoadingPos(new ClientboundAuraPacket(entity.getId(), attachment.getAllAuraValues(), attachment.getPrimaryAuraType()), level, entity.blockPosition());
		}
	}

	/**
	 * Registers the aura attachment for the provided entity classes.
	 * This should be called in your mod's main class.
	 * @param clazz The classes for entities that should have aura
	 */
	@SafeVarargs
	public static void registerAuraProviders(Class<? extends Entity>... clazz) {
		AURA_PROVIDERS.addAll(Arrays.asList(clazz));
	}

	/**
	 * @param entity The entity being checked for if it should have AuraAttachment data.
	 * @return If an entity should have AuraAttachment data.
	 */
	public static boolean isAuraProvider(Entity entity) {
		for(Class<? extends Entity> clazz : AURA_PROVIDERS)
			if(clazz.isInstance(entity))
				return true;

		return false;
	}
}
