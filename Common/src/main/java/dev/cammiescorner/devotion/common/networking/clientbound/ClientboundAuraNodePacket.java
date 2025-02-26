package dev.cammiescorner.devotion.common.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;
import java.util.Map;

public record ClientboundAuraNodePacket(ChunkPos chunkPos, Map<BlockPos, AuraNode> auraNodeMap) implements CustomPacketPayload {
	public static final Type<ClientboundAuraNodePacket> TYPE = new Type<>(Devotion.id("aura_node"));
	public static final StreamCodec<? extends FriendlyByteBuf, ClientboundAuraNodePacket> CODEC = StreamCodec.of((buffer, value) -> {
		buffer.writeVarInt(value.chunkPos.x);
		buffer.writeVarInt(value.chunkPos.z);
		buffer.writeVarInt(value.auraNodeMap.size());

		for(BlockPos blockPos : value.auraNodeMap.keySet()) {
			AuraNode node = value.auraNodeMap.get(blockPos);

			buffer.writeBlockPos(blockPos);
			buffer.writeMap(node.viewAuraMap(), FriendlyByteBuf::writeEnum, FriendlyByteBuf::writeFloat);
		}
	}, buffer -> {
		ChunkPos chunkPos = new ChunkPos(buffer.readVarInt(), buffer.readVarInt());
		Map<BlockPos, AuraNode> auraNodeMap = new HashMap<>();
		int mapSize = buffer.readVarInt();

		for(int i = 0; i < mapSize; i++) {
			BlockPos pos = buffer.readBlockPos();
			Map<AuraType, Float> auraMap = buffer.readMap(byteBuf -> byteBuf.readEnum(AuraType.class), FriendlyByteBuf::readFloat);
			AuraNode node = new AuraNode(auraMap);

			auraNodeMap.put(pos, node);
		}

		return new ClientboundAuraNodePacket(chunkPos, auraNodeMap);
	});

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ClientboundAuraNodePacket> context) {
		ClientLevel level = Minecraft.getInstance().level;
		ChunkPos chunkPos = context.message().chunkPos;
		Map<BlockPos, AuraNode> auraNodeMap = context.message().auraNodeMap;

		if(level != null) {
			LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);

			for(Map.Entry<BlockPos, AuraNode> entry : auraNodeMap.entrySet())
				MainHelper.addAuraNode(chunk, entry.getKey(), entry.getValue());
		}
	}
}
