package dev.cammiescorner.devotion.common.packets.c2s;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.packets.s2c.RefreshResearchScreenPacket;
import dev.cammiescorner.devotion.common.screens.ResearchScreenHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
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
			buf.writeString(type.toString());

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		int syncId = buf.readVarInt();
		int listSize = buf.readVarInt();
		NbtList nbtList = new NbtList();

		for(int i = 0; i < listSize; i++)
			nbtList.add(NbtString.of(buf.readString()));

		server.execute(() -> {
			if(player.currentScreenHandler.syncId == syncId && player.currentScreenHandler instanceof ResearchScreenHandler screenHandler) {
				screenHandler.getContext().run((world, pos) -> {
					if(world.getBlockEntity(pos) instanceof LecternBlockEntity lectern) {
						NbtCompound tag = lectern.getBook().getOrCreateNbt();

						if(tag.getList("AuraTypes", NbtElement.STRING_TYPE) != null)
							tag.remove("AuraTypes");

						tag.put("AuraTypes", nbtList);
						lectern.markDirty();

						RefreshResearchScreenPacket.send(PlayerLookup.tracking(lectern).stream().filter(serverPlayerEntity -> serverPlayerEntity != player && serverPlayerEntity.currentScreenHandler instanceof ResearchScreenHandler).toList(), lectern.getBook());
					}
				});
			}
		});
	}
}
