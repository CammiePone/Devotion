package dev.cammiescorner.devotion.mixin;

import dev.cammiescorner.devotion.common.items.ResearchScrollItem;
import dev.cammiescorner.devotion.common.screens.ResearchScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin extends BlockEntity implements Clearable, NamedScreenHandlerFactory {
	@Shadow @Final private Inventory inventory;
	@Shadow public abstract ItemStack getBook();

	public LecternBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Inject(method = "createMenu", at = @At("HEAD"), cancellable = true)
	private void devotion$createResearchScreen(int i, PlayerInventory playerInventory, PlayerEntity playerEntity, CallbackInfoReturnable<ScreenHandler> info) {
		if(getBook().getItem() instanceof ResearchScrollItem)
			info.setReturnValue(new ResearchScreenHandler(i, inventory));
	}

	@Inject(method = "setBook(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("TAIL"))
	private void devotion$syncContents(ItemStack book, PlayerEntity player, CallbackInfo info) {
		if(world != null && !world.isClient())
			world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_ALL);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound tag = super.toInitialChunkDataNbt();
		writeNbt(tag);
		return tag;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}
}
