package dev.cammiescorner.devotion.common.components.chunk;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class PurpleWaterComponent implements AutoSyncedComponent {
	private final Chunk chunk;
	private final Object2ObjectMap<BlockPos, BlockPos> waterMap = new Object2ObjectOpenHashMap<>();

	public PurpleWaterComponent(Chunk chunk) {
		this.chunk = chunk;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList nbtList = tag.getList("WaterMap", NbtElement.COMPOUND_TYPE);

		waterMap.clear();
		for(int i = 0; i < nbtList.size(); i++) {
			NbtCompound map = nbtList.getCompound(i);
			waterMap.put(
					NbtHelper.toBlockPos(map.getCompound("WaterPos")),
					NbtHelper.toBlockPos(map.getCompound("AltarPos"))
			);
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList nbtList = new NbtList();

		waterMap.forEach((waterPos, altarPos) -> {
			NbtCompound map = new NbtCompound();
			map.put("WaterPos", NbtHelper.fromBlockPos(waterPos));
			map.put("AltarPos", NbtHelper.fromBlockPos(altarPos));
			nbtList.add(map);
		});

		tag.put("WaterMap", nbtList);
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		buf.writeVarInt(this.waterMap.size());
		for (var entry : this.waterMap.entrySet()) {
			buf.writeBlockPos(entry.getKey());
			buf.writeBlockPos(entry.getValue());
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		MinecraftClient.getInstance().getProfiler().push("Purple Water Chunk Sync");

		var waterMapHash = this.waterMap.hashCode();

		this.waterMap.clear();
		int amount = buf.readVarInt();
		for (int i = 0; i < amount; i++) {
			this.waterMap.put(buf.readBlockPos(), buf.readBlockPos());
		}

		if (waterMapHash != this.waterMap.hashCode()) {
			ChunkPos pos = chunk.getPos();
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getStartX(), chunk.getBottomY(), pos.getStartZ(), pos.getEndX(), chunk.getTopY(), pos.getEndZ());
		}

		MinecraftClient.getInstance().getProfiler().pop();
	}

	public void addAltar(BlockPos pos, BlockPos altarPos) {
		waterMap.put(pos, altarPos);
	}

	public void removeAltar(BlockPos altarPos) {
		waterMap.entrySet().removeIf(entry -> entry.getValue().equals(altarPos));
	}

	public boolean isPosNearAltar(BlockPos pos) {
		return waterMap.containsKey(pos);
	}

	public Chunk getChunk() {
		return chunk;
	}
}
