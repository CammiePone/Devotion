package dev.cammiescorner.devotion.common.networking.serverbound;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.research.RiddleData;
import dev.cammiescorner.devotion.common.items.ResearchScrollItem;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public record ServerboundGiveResearchScrollPacket(ResourceKey<Research> researchId) implements CustomPacketPayload {
	public static final Type<ServerboundGiveResearchScrollPacket> TYPE = new Type<>(Devotion.id("give_research_scroll"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundGiveResearchScrollPacket> CODEC = StreamCodec.of(
		(buffer, packet) -> buffer.writeResourceKey(packet.researchId()),
		buffer -> new ServerboundGiveResearchScrollPacket(buffer.readResourceKey(DevotionRegistries.RESEARCH))
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ServerboundGiveResearchScrollPacket> context) {
		ResourceKey<Research> researchId = context.message().researchId();
		ServerPlayer player = context.sender();
		boolean bl = true;

		for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			DataComponentType<Holder<Research>> researchData = DevotionData.RESEARCH.get();

			if(stack.get(researchData) instanceof Holder<Research> research && research.unwrapKey().orElseThrow().location().equals(researchId)) {
				bl = false;
				break;
			}
		}

		if(bl) {
			HolderLookup.RegistryLookup<Research> lookUp = context.sender().registryAccess().lookupOrThrow(DevotionRegistries.RESEARCH);
			Holder<Research> research = lookUp.getOrThrow(researchId);
			ItemStack stack = new ItemStack(DevotionItems.RESEARCH_SCROLL.get());
			RiddleData riddleData = ResearchScrollItem.generateRiddleData(research, player.getRandom());

			stack.set(DevotionData.RIDDLE_DATA.get(), riddleData);
			stack.set(DevotionData.RESEARCH.get(), research);

			boolean canInsert = player.getInventory().add(stack);
			ItemEntity itemEntity;

			if(!canInsert || !stack.isEmpty()) {
				itemEntity = player.drop(stack, false);

				if(itemEntity == null)
					return;

				itemEntity.setNoPickUpDelay();
				itemEntity.setTarget(player.getUUID());
			}

			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1f) * 2f);
		}
	}
}
