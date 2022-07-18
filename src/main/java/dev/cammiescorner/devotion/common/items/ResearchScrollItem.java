package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.api.spells.AuraType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
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
			List<Pair<AuraType, Integer>> list = new ArrayList<>();
			int maxRiddles = player.getRandom().nextInt(5) + 4;

			for(int i = 0; i < maxRiddles; i++) {
				AuraType type, prevType1 = i < 1 ? null : list.get(i - 1).getLeft(), prevType2 = i < 2 ? null : list.get(i - 2).getLeft();

				do type = AuraType.values()[player.getRandom().nextInt(6)];
				while(type.equals(prevType1) || type.equals(prevType2));

				list.add(new Pair<>(type, player.getRandom().nextInt(9)));
			}

			for(Pair<AuraType, Integer> pair : list) {
				NbtCompound compound = new NbtCompound();
				compound.putInt("AuraTypeIndex", pair.getLeft().ordinal());
				compound.putInt("RiddleIndex", pair.getRight());

				nbtList.add(compound);
			}

			tag.put("RiddleList", nbtList);
		}

		return super.use(world, player, hand);
	}
}
