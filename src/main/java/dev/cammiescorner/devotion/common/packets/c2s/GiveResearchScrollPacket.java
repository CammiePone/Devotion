package dev.cammiescorner.devotion.common.packets.c2s;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.items.ResearchScrollItem;
import dev.cammiescorner.devotion.common.registry.DevotionItems;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class GiveResearchScrollPacket {
	public static final Identifier ID = Devotion.id("give_research_scroll");

	public static void send(Identifier researchId) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeIdentifier(researchId);

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		Identifier researchId = buf.readIdentifier();

		server.execute(() -> {
			boolean bl = true;

			for(int i = 0; i < player.getInventory().size(); i++) {
				ItemStack stack = player.getInventory().getStack(i);
				NbtCompound tag = stack.getOrCreateSubNbt(Devotion.MOD_ID);

				if(tag.getString("ResearchId").equals(researchId.toString())) {
					bl = false;
					break;
				}
			}

			if(bl) {
				Research research = Research.getById(researchId);
				Research.Difficulty difficulty = research != null ? research.getDifficulty() : Research.Difficulty.EASY;
				ItemStack stack = new ItemStack(DevotionItems.RESEARCH_SCROLL);
				NbtCompound tag = stack.getOrCreateSubNbt(Devotion.MOD_ID);
				NbtList nbtList = new NbtList();
				int maxRiddles = difficulty == Research.Difficulty.EASY ? 4 : difficulty == Research.Difficulty.NORMAL ? 6 : 8;

				for(Pair<AuraType, Integer> pair : ResearchScrollItem.generateRiddleList(player.getRandom(), maxRiddles)) {
					NbtCompound compound = new NbtCompound();
					compound.putInt("AuraTypeIndex", pair.getLeft().ordinal());
					compound.putInt("RiddleIndex", pair.getRight());

					nbtList.add(compound);
				}

				tag.put("RiddleList", nbtList);
				tag.putString("ResearchId", researchId.toString());

				boolean canInsert = player.getInventory().insertStack(stack);
				ItemEntity itemEntity;

				if(!canInsert || !stack.isEmpty()) {
					itemEntity = player.dropItem(stack, false);

					if(itemEntity == null)
						return;

					itemEntity.resetPickupDelay();
					itemEntity.setOwner(player.getUuid());
				}

				player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1F) * 2F);
			}
		});
	}
}
