package dev.cammiescorner.devotion.common.items;

import com.mojang.datafixers.util.Pair;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.Graph;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.research.RiddleData;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ResearchScrollItem extends Item {
	public ResearchScrollItem() {
		super(new Properties().stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		if(!level.isClientSide()) {
			ItemStack stack = player.getItemInHand(usedHand);

			if(stack.getOrDefault(DevotionData.SCROLL_COMPLETED.get(), false) && stack.has(DevotionData.RESEARCH.get())) {
				Holder<Research> research = stack.get(DevotionData.RESEARCH.get());

				if(research != null && MainHelper.getResearchIds(player).containsAll(research.value().parentIds())) {
					if(MainHelper.giveResearch(player, research.value(), false)) {
						stack.consume(1, player);
						level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.25f, 1f);
						return InteractionResultHolder.success(stack);
					}
					else {
						player.displayClientMessage(Component.translatable("research_error." + Devotion.MOD_ID + ".already_known").withStyle(ChatFormatting.RED), true);
						return InteractionResultHolder.fail(stack);
					}
				}
				else {
					player.displayClientMessage(Component.translatable("research_error." + Devotion.MOD_ID + ".dont_know_parents").withStyle(ChatFormatting.RED), true);
					return InteractionResultHolder.fail(stack);
				}
			}
			else {
				return InteractionResultHolder.fail(stack);
			}
		}

		return super.use(level, player, usedHand);
	}

	@Override
	public Component getName(ItemStack stack) {
		DataComponentType<Holder<Research>> data = DevotionData.RESEARCH.get();

		if(stack.get(data) instanceof Holder<Research> research)
			return super.getName(stack).copy().append(" (").append(Component.translatable(Util.makeDescriptionId("devotion_research", research.unwrapKey().orElseThrow().location()))).append(")");

		return super.getName(stack);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getOrDefault(DevotionData.SCROLL_COMPLETED.get(), false);
	}

	public static RiddleData generateRiddleData(Holder<Research> research, RandomSource random) {
		List<Graph.Node<AuraType>> path = new ArrayList<>();
		HashSet<Graph.Edge<AuraType>> visitedEdges = new HashSet<>();
		Research.Difficulty difficulty = research != null ? research.value().difficulty() : Research.Difficulty.EASY;
		int maxRiddles = difficulty.getRiddleCount();

		// pick random starting node
		path.add(Devotion.AURA_GRAPH.nodes.get(random.nextInt(Devotion.AURA_GRAPH.nodes.size())));

		while(path.size() < maxRiddles) {
			Graph.Node<AuraType> current = path.getLast();
			List<Graph.Edge<AuraType>> connections = current.getConnections();

			if(visitedEdges.containsAll(connections)) {
				// path too short, go back 1 and try again
				path.removeLast();
				visitedEdges.removeIf(e -> e.nodes.contains(current) && e.nodes.contains(path.getLast()));
				continue;
			}

			Graph.Edge<AuraType> next = connections.get(random.nextInt(connections.size()));

			if(visitedEdges.contains(next))
				continue;

			visitedEdges.add(next);
			path.add(next.nodes.stream().filter(n -> n != current).findFirst().orElseThrow(() -> new IllegalStateException("invalid graph")));
		}

		return new RiddleData(path.stream().map(auraTypeNode -> Pair.of(auraTypeNode.obj, random.nextInt(10))).toList());
	}
}
