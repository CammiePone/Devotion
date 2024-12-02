package dev.cammiescorner.devotion.neoforge.common.attachments.entity;

import com.google.common.collect.ImmutableSet;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundKnownResearchPacket;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class KnownResearchAttachment implements INBTSerializable<CompoundTag> {
	private final Set<ResourceLocation> researchIds = new HashSet<>();

	public KnownResearchAttachment() { }

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		ListTag nbtList = tag.getList("KnownResearch", ListTag.TAG_STRING);
		researchIds.clear();

		for(int i = 0; i < nbtList.size(); i++)
			researchIds.add(ResourceLocation.parse(nbtList.getString(i)));
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();
		ListTag nbtList = new ListTag();

		for(ResourceLocation researchId : researchIds)
			nbtList.add(StringTag.valueOf(researchId.toString()));

		tag.put("KnownResearch", nbtList);

		return tag;
	}

	public Set<ResourceLocation> getResearchIds() {
		return ImmutableSet.copyOf(researchIds);
	}

	public void addResearch(ResourceLocation id) {
		researchIds.add(id);
	}

	public void revokeResearch(ResourceLocation id) {
		researchIds.remove(id);
	}

	public static void sync(Player player) {
		if(player instanceof ServerPlayer serverPlayer && serverPlayer.hasData(NeoMain.KNOWN_RESEARCH))
			Network.getNetworkHandler().sendToClient(new ClientboundKnownResearchPacket(serverPlayer.getData(NeoMain.KNOWN_RESEARCH).getResearchIds()), serverPlayer);
	}
}
