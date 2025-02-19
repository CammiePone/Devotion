package dev.cammiescorner.devotion.fabric.common.components.chunk;

import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;

public class AuraNodeComponent implements AutoSyncedComponent {
	private final Map<BlockPos, AuraNode> auraNodeMap = new HashMap<>();
	private final ChunkAccess access;
	private float auraAffinity;
	private int maxAuraNodes;

	public AuraNodeComponent(ChunkAccess access) {
		RandomGenerator random = RandomGenerator.getDefault();

		this.access = access;
		this.maxAuraNodes = Math.max(random.nextInt(-50, 2), 0);
		this.auraAffinity = random.nextFloat();
	}

	@Override
	public void readFromNbt(CompoundTag compoundTag, HolderLookup.Provider provider) {
		ListTag listTag = compoundTag.getList("AuraNodes", Tag.TAG_COMPOUND);

		auraNodeMap.clear();
		auraAffinity = compoundTag.getFloat("AuraAffinity");
		maxAuraNodes = compoundTag.getInt("MaxAuraNodes");

		for(int i = 0; i < listTag.size(); i++) {
			CompoundTag tag = listTag.getCompound(i);

			NbtUtils.readBlockPos(tag, "BlockPos").ifPresent(pos -> auraNodeMap.put(pos, AuraNode.readAuraNode(tag, "AuraNode")));
		}
	}

	@Override
	public void writeToNbt(CompoundTag compoundTag, HolderLookup.Provider provider) {
		ListTag listTag = new ListTag();

		for(BlockPos pos : auraNodeMap.keySet()) {
			CompoundTag tag = new CompoundTag();

			tag.put("BlockPos", NbtUtils.writeBlockPos(pos));
			auraNodeMap.get(pos).writeAuraNode(tag, "AuraNode");

			listTag.add(tag);
		}

		compoundTag.put("AuraNodes", listTag);
		compoundTag.putFloat("AuraAffinity", auraAffinity);
		compoundTag.putInt("MaxAuraNodes", maxAuraNodes);
	}

	@Override
	public void applySyncPacket(RegistryFriendlyByteBuf buf) {
		int auraMapSize = buf.readVarInt();

		auraNodeMap.clear();

		for(int i = 0; i < auraMapSize; i++) {
			BlockPos pos = buf.readBlockPos();
			AuraNode node = new AuraNode(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());

			auraNodeMap.put(pos, node);
		}
	}

	@Override
	public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
		buf.writeVarInt(auraNodeMap.size());

		for(BlockPos blockPos : auraNodeMap.keySet()) {
			AuraNode node = auraNodeMap.get(blockPos);

			buf.writeBlockPos(blockPos);
			buf.writeFloat(node.getEnhancementAura());
			buf.writeFloat(node.getTransmutationAura());
			buf.writeFloat(node.getEmissionAura());
			buf.writeFloat(node.getConjurationAura());
			buf.writeFloat(node.getManipulationAura());
		}
	}

	public void addAuraNode(BlockPos pos, AuraNode node) {
		ChunkPos chunkPos = access.getPos();
		auraNodeMap.put(new BlockPos(chunkPos.x != 0 ? pos.getX() % chunkPos.x : pos.getX(), pos.getY(), chunkPos.z != 0 ? pos.getZ() % chunkPos.z : pos.getZ()), node);
		DevotionComponents.AURA_NODE.sync(access);
	}

	public void removeAuraNode(BlockPos pos) {
		auraNodeMap.remove(pos);
		DevotionComponents.AURA_NODE.sync(access);
	}

	public Map<BlockPos, AuraNode> getAuraNodeMap() {
		return Map.copyOf(auraNodeMap);
	}

	public int getMaxAuraNodes() {
		return maxAuraNodes;
	}

	public float getAuraAffinity() {
		return auraAffinity;
	}
}
