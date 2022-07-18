package dev.cammiescorner.devotion.common.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResearchScrollItem extends Item {
	public ResearchScrollItem() {
		super(new QuiltItemSettings().maxCount(1));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);

		if(state.isOf(Blocks.LECTERN))
			return LecternBlock.putBookIfAbsent(context.getPlayer(), world, pos, state, context.getStack()) ? ActionResult.success(world.isClient) : ActionResult.PASS;

		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		if(!stack.hasNbt()) {
			NbtCompound tag = stack.getOrCreateNbt();
			NbtList nbtList = new NbtList();
			List<String> list = new ArrayList<>(List.of("enhancement", "transmutation", "emission", "conjuration", "manipulation", "specialisation"));
			Collections.shuffle(list);

			for(String s : list)
				nbtList.add(NbtString.of(s));

			tag.put("RiddleList", nbtList);
		}

		return super.use(world, player, hand);
	}
}
