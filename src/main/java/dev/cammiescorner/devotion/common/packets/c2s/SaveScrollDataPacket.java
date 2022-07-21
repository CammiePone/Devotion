package dev.cammiescorner.devotion.common.packets.c2s;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.packets.s2c.RefreshResearchScreenPacket;
import dev.cammiescorner.devotion.common.screens.ResearchScreenHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.util.List;

public class SaveScrollDataPacket {
	public static final Identifier ID = Devotion.id("save_scroll_data");

	public static void send(int syncId, List<AuraType> auraTypes) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(syncId);
		buf.writeVarInt(auraTypes.size());

		for(AuraType type : auraTypes)
			buf.writeVarInt(type.ordinal());

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		int syncId = buf.readVarInt();
		int listSize = buf.readVarInt();
		NbtList auraTypeList = new NbtList();

		for(int i = 0; i < listSize; i++)
			auraTypeList.add(NbtInt.of(buf.readVarInt()));

		server.execute(() -> {
			if(player.currentScreenHandler.syncId == syncId && player.currentScreenHandler instanceof ResearchScreenHandler screenHandler) {
				screenHandler.getContext().run((world, pos) -> {
					if(world.getBlockEntity(pos) instanceof LecternBlockEntity lectern) {
						NbtCompound tag = lectern.getBook().getOrCreateSubNbt(Devotion.MOD_ID);

						if(tag.getList("AuraTypes", NbtElement.INT_TYPE) != null)
							tag.remove("AuraTypes");

						tag.put("AuraTypes", auraTypeList);

						NbtList riddleList = tag.getList("RiddleList", NbtElement.COMPOUND_TYPE);

						for(int i = 0; i < riddleList.size(); i++) {
							if(riddleList.getCompound(i).getInt("AuraTypeIndex") != auraTypeList.getInt(i)) {
								break;
							}
							else if(riddleList.size() == auraTypeList.size() && (!tag.contains("Completed") || !tag.getBoolean("Completed"))) {
								tag.putBoolean("Completed", true);
								tag.putLong("TimeCompleted", world.getTime());
							}
						}

						lectern.markDirty();
						world.updateListeners(lectern.getPos(), lectern.getCachedState(), lectern.getCachedState(), Block.NOTIFY_ALL);

						RefreshResearchScreenPacket.send(PlayerLookup.tracking(lectern).stream().filter(serverPlayerEntity -> serverPlayerEntity.currentScreenHandler instanceof ResearchScreenHandler).toList(), lectern.getBook());
					}
				});
			}
		});
	}
}
