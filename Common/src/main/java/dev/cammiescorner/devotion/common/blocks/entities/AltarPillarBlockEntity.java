package dev.cammiescorner.devotion.common.blocks.entities;

import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.registries.DevotionBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AltarPillarBlockEntity extends BlockEntity {
	private static final float MAX_AURA = 1000f;
	private final List<BlockState> storedBlocks = new ArrayList<>();
	private BlockPos altarFocusPos = BlockPos.ZERO;
	private AuraType containedAuraType = AuraType.NONE;
	private float storedAura = 0f;

	public AltarPillarBlockEntity(BlockPos pos, BlockState blockState) {
		super(DevotionBlocks.ALTAR_PILLAR_ENTITY.get(), pos, blockState);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		storedBlocks.clear();

		ListTag listTag = tag.getList("StoredBlocks", Tag.TAG_COMPOUND);

		for(int i = 0; i < listTag.size(); i++)
			storedBlocks.add(NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), listTag.getCompound(i)));

		NbtUtils.readBlockPos(tag, "AltarFocusPos").ifPresent(pos -> altarFocusPos = pos);
		containedAuraType = AuraType.valueOf(tag.getString("ContainedAuraType"));
		storedAura = tag.getFloat("StoredAura");
		super.loadAdditional(tag, registries);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		ListTag listTag = new ListTag();

		for(BlockState state : storedBlocks)
			listTag.add(NbtUtils.writeBlockState(state));

		tag.put("StoredBlocks", listTag);
		tag.put("AltarFocusPos", NbtUtils.writeBlockPos(altarFocusPos));
		tag.putString("ContainedAuraType", containedAuraType.name());
		tag.putFloat("StoredAura", storedAura);
		super.saveAdditional(tag, registries);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);
		saveAdditional(tag, registries);

		return tag;
	}

	@Override
	public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public void notifyListeners() {
		if(level != null && !level.isClientSide()) {
			setChanged();
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}

	public List<BlockState> getStoredBlocks() {
		return Collections.unmodifiableList(storedBlocks);
	}

	public void setStoredBlocks(List<BlockState> states) {
		storedBlocks.clear();
		storedBlocks.addAll(states);
		setChanged();
	}

	public BlockPos getAltarFocusPos() {
		return altarFocusPos;
	}

	public void setAltarFocusPos(BlockPos pos) {
		altarFocusPos = pos;
		notifyListeners();
	}

	public AuraType getContainedAuraType() {
		return containedAuraType;
	}

	public float getAuraAlpha() {
		return getStoredAura() / MAX_AURA;
	}

	public float getStoredAura() {
		return storedAura;
	}

	public boolean addAura(float amount, boolean simulate) {
		if(storedAura < MAX_AURA) {
			if(!simulate) {
				storedAura += amount;
				notifyListeners();
			}

			return true;
		}

		return false;
	}

	public boolean drainAura(float amount, boolean simulate) {
		if(storedAura - amount >= 0) {
			if(!simulate) {
				storedAura -= amount;
				notifyListeners();
			}

			return true;
		}

		return false;
	}
}
