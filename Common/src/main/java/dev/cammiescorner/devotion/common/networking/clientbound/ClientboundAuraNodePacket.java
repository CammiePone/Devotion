package dev.cammiescorner.devotion.common.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;
import java.util.Map;

// TODO remove printfs later once problem is solved
public record ClientboundAuraNodePacket(ChunkPos chunkPos, Map<BlockPos, AuraNode> auraNodeMap) implements CustomPacketPayload {
	public static final Type<ClientboundAuraNodePacket> TYPE = new Type<>(Devotion.id("aura_node"));
	public static final StreamCodec<? extends FriendlyByteBuf, ClientboundAuraNodePacket> CODEC = StreamCodec.of((buffer, value) -> {
		buffer.writeVarInt(value.chunkPos.x);
		buffer.writeVarInt(value.chunkPos.z);
		buffer.writeVarInt(value.auraNodeMap.size());

		for(BlockPos blockPos : value.auraNodeMap.keySet()) {
			AuraNode auraNode = value.auraNodeMap.get(blockPos);

			buffer.writeBlockPos(blockPos);
			buffer.writeFloat(auraNode.getEnhancementAura());
			buffer.writeFloat(auraNode.getTransmutationAura());
			buffer.writeFloat(auraNode.getEmissionAura());
			buffer.writeFloat(auraNode.getConjurationAura());
			buffer.writeFloat(auraNode.getManipulationAura());
		}
	}, buffer -> {
		ChunkPos chunkPos = new ChunkPos(buffer.readVarInt(), buffer.readVarInt());
		Map<BlockPos, AuraNode> auraNodeMap = new HashMap<>();
		int mapSize = buffer.readVarInt();

		for(int i = 0; i < mapSize; i++) {
			BlockPos blockPos = buffer.readBlockPos();
			AuraNode auraNode = new AuraNode(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());

			auraNodeMap.put(blockPos, auraNode);
		}

		return new ClientboundAuraNodePacket(chunkPos, auraNodeMap);
	});

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ClientboundAuraNodePacket> context) {
		ClientLevel level = DevotionClient.client.level;
		ChunkPos chunkPos = context.message().chunkPos;
		Map<BlockPos, AuraNode> auraNodeMap = context.message().auraNodeMap;

		if(level != null) {
			LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);

			for(Map.Entry<BlockPos, AuraNode> entry : auraNodeMap.entrySet())
				MainHelper.addAuraNode(chunk, entry.getKey(), entry.getValue());
		}
	}
}
