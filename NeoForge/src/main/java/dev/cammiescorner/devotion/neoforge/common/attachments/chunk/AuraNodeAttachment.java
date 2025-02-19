package dev.cammiescorner.devotion.neoforge.common.attachments.chunk;

import dev.cammiescorner.devotion.api.world.AuraNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;

public class AuraNodeAttachment implements INBTSerializable<CompoundTag> {
	private final Map<BlockPos, AuraNode> auraNodeMap = new HashMap<>();
	private final RandomGenerator random = RandomGenerator.getDefault();
	private final ChunkAccess access;
	private float auraAffinity;
	private int maxAuraNodes;

	public AuraNodeAttachment(IAttachmentHolder holder) {
		this.access = holder instanceof ChunkAccess access ? access : null;
		this.maxAuraNodes = Math.max(random.nextInt(-50, 2), 0);
		this.auraAffinity = random.nextFloat();
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
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
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag compoundTag = new CompoundTag();
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

		return compoundTag;
	}

	public void addAuraNode(BlockPos pos, AuraNode node) {
		auraNodeMap.put(pos, node);
	}

	public RandomGenerator getRandom() {
		return random;
	}

	public Map<BlockPos, AuraNode> getAuraNodeMap() {
		return Map.copyOf(auraNodeMap);
	}

	public float getAuraAffinity() {
		return auraAffinity;
	}

	public int getMaxAuraNodes() {
		return maxAuraNodes;
	}
}
