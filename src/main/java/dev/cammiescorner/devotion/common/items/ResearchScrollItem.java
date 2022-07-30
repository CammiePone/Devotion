package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.api.Graph;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
import java.util.HashSet;
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
	public Text getName(ItemStack stack) {
		NbtCompound tag = stack.getSubNbt(Devotion.MOD_ID);

		if(tag != null && tag.contains("ResearchId")) {
			Research research = Research.getById(new Identifier(tag.getString("ResearchId")));
			return super.getName(stack).copy().append(" (").append(Text.translatable(research.getTranslationKey())).append(")");
		}

		return super.getName(stack);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if(!world.isClient()) {
			ItemStack stack = player.getStackInHand(hand);
			NbtCompound tag = stack.getOrCreateSubNbt(Devotion.MOD_ID);

			if(tag.getBoolean("Completed")) {
				Identifier researchId = new Identifier(tag.getString("ResearchId"));
				Research research = Research.getById(researchId);

				if(DevotionHelper.getResearchIds(player).containsAll(research.getParentIds())) {
					if(DevotionHelper.giveResearch(player, research, true)) {
						if(player.isCreative() || DevotionHelper.drainAura(player, tag.getList("RiddleList", NbtElement.COMPOUND_TYPE).size(), false)) {
							DevotionHelper.giveResearch(player, research, false);
							stack.decrement(1);
							return TypedActionResult.success(stack);
						}
						else {
							player.sendMessage(Text.translatable("research_error.devotion.not_enough_aura").formatted(Formatting.RED), true);
							return TypedActionResult.fail(stack);
						}
					}
					else {
						player.sendMessage(Text.translatable("research_error.devotion.already_known").formatted(Formatting.RED), true);
						return TypedActionResult.fail(stack);
					}
				}
				else {
					player.sendMessage(Text.translatable("research_error.devotion.dont_know_parents").formatted(Formatting.RED), true);
					return TypedActionResult.fail(stack);
				}
			}
			else {
				return TypedActionResult.fail(stack);
			}
		}

		return super.use(world, player, hand);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return stack.hasNbt() && stack.getSubNbt(Devotion.MOD_ID).getBoolean("Completed");
	}

	/**
	 * @author UpcraftLP
	 */
	public static List<Pair<AuraType, Integer>> generateRiddleList(RandomGenerator random, int maxRiddleLength) {
		List<Graph.Node<AuraType>> path = new ArrayList<>();
		HashSet<Graph.Edge<AuraType>> visitedEdges = new HashSet<>();

		// pick random starting node
		path.add(Devotion.AURA_GRAPH.nodes.get(random.nextInt(Devotion.AURA_GRAPH.nodes.size())));

		while(path.size() < maxRiddleLength) {
			Graph.Node<AuraType> current = path.get(path.size() - 1);
			List<Graph.Edge<AuraType>> connections = current.getConnections();

			if(visitedEdges.containsAll(connections)) {
				// path too short, go back 1 and try again
				path.remove(path.size() - 1);
				visitedEdges.removeIf(e -> e.nodes.contains(current) && e.nodes.contains(path.get(path.size() - 1)));
				continue;
			}

			Graph.Edge<AuraType> next = connections.get(random.nextInt(connections.size()));

			if(visitedEdges.contains(next))
				continue;

			visitedEdges.add(next);
			path.add(next.nodes.stream().filter(n -> n != current).findFirst().orElseThrow(() -> new IllegalStateException("invalid graph")));
		}

		return path.stream().map(auraTypeNode -> new Pair<>(auraTypeNode.obj, random.nextInt(9))).toList();
	}
}
