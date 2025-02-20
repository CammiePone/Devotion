package dev.cammiescorner.devotion.common.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public record ClientboundAuraPacket(int entityId, Map<AuraType, Float> aura, AuraType primaryAuraType) implements CustomPacketPayload {
	public static final Type<ClientboundAuraPacket> TYPE = new Type<>(Devotion.id("aura"));
	public static final StreamCodec<? extends FriendlyByteBuf, ClientboundAuraPacket> CODEC = StreamCodec.of((buffer, value) -> {
		buffer.writeVarInt(value.entityId);
		buffer.writeVarInt(value.aura.entrySet().size());
		buffer.writeUtf(value.primaryAuraType.getSerializedName());

		for(Map.Entry<AuraType, Float> entry : value.aura.entrySet()) {
			buffer.writeUtf(entry.getKey().getSerializedName());
			buffer.writeFloat(entry.getValue());
		}
	}, buffer -> {
		int entityId = buffer.readVarInt();
		int mapSize = buffer.readVarInt();
		AuraType primaryAuraType = AuraType.byName(buffer.readUtf());
		Map<AuraType, Float> aura = new HashMap<>();

		for(int i = 0; i < mapSize; i++) {
			AuraType auraType = AuraType.byName(buffer.readUtf());
			float auraAmount = buffer.readFloat();
			aura.put(auraType, auraAmount);
		}

		return new ClientboundAuraPacket(entityId, aura, primaryAuraType);
	});

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ClientboundAuraPacket> context) {
		ClientLevel level = Minecraft.getInstance().level;
		int entityId = context.message().entityId;
		Map<AuraType, Float> aura = context.message().aura;
		AuraType primaryAuraType = context.message().primaryAuraType;

		if(level != null && level.getEntity(entityId) instanceof LivingEntity entity) {
			for(Map.Entry<AuraType, Float> entry : aura.entrySet())
				MainHelper.setAura(entity, entry.getKey(), entry.getValue());

			MainHelper.setPrimaryAuraType(entity, primaryAuraType);
		}
	}
}
