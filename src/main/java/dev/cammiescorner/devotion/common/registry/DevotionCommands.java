package dev.cammiescorner.devotion.common.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.common.components.entity.AuraComponent;
import dev.cammiescorner.devotion.common.components.entity.CurrentSpellComponent;
import dev.cammiescorner.devotion.common.components.entity.SpellInventoryComponent;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.SingletonArgumentInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class DevotionCommands {
	public static void register() {
		ArgumentTypeRegistry.registerArgumentType(Devotion.id("spell"), SpellArgumentType.class, SingletonArgumentInfo.contextFree(SpellArgumentType::new));
	}

	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext commandBuildContext, CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(CommandManager.literal("spell")
				.then(CommandManager.literal("set")
						.then(CommandManager.argument("spell", SpellArgumentType.spell())
								.executes(SpellsCommand::setSpell)
						)
				)
				.then(CommandManager.literal("get")
						.executes(SpellsCommand::getSpell)
				)
		);
		dispatcher.register(CommandManager.literal("drain_aura").executes(AuraCommand::drainAura));
	}

	public static class SpellArgumentType implements ArgumentType<Spell> {
		public static final DynamicCommandExceptionType INVALID_SPELL_EXCEPTION = new DynamicCommandExceptionType(object -> Text.translatable("commands." + Devotion.MOD_ID + ".spells.not_found", object));

		public static SpellArgumentType spell() {
			return new SpellArgumentType();
		}

		public static Spell getSpell(CommandContext<ServerCommandSource> commandContext, String string) {
			return commandContext.getArgument(string, Spell.class);
		}

		@Override
		public Spell parse(StringReader reader) throws CommandSyntaxException {
			Identifier identifier = Identifier.fromCommandInput(reader);
			return Devotion.SPELL.getOrEmpty(identifier).orElseThrow(() -> INVALID_SPELL_EXCEPTION.create(identifier));
		}

		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
			return CommandSource.suggestIdentifiers(Devotion.SPELL.getIds(), builder);
		}
	}

	private static class SpellsCommand {
		public static int setSpell(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
			PlayerEntity player = context.getSource().getPlayer();
			SpellInventoryComponent spellComponent = DevotionComponents.SPELL_INVENTORY_COMPONENT.get(player);
			Spell spell = DevotionCommands.SpellArgumentType.getSpell(context, "spell");

			spellComponent.setSpellInSlot(spell, 0);
			context.getSource().sendFeedback(Text.literal("Set spell to: ").append(Text.literal(Devotion.SPELL.getId(spell).toString()).formatted(Formatting.YELLOW)), false);

			return Command.SINGLE_SUCCESS;
		}

		public static int getSpell(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
			PlayerEntity player = context.getSource().getPlayer();
			CurrentSpellComponent spellComponent = DevotionComponents.CURRENT_SPELL_COMPONENT.get(player);
			Spell spell = spellComponent.getSelectedSpell();

			context.getSource().sendFeedback(Text.literal("Current spell: ").append(Text.literal(Devotion.SPELL.getId(spell).toString()).formatted(Formatting.YELLOW)), false);

			return Command.SINGLE_SUCCESS;
		}
	}

	private static class AuraCommand {
		public static int drainAura(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
			PlayerEntity player = context.getSource().getPlayer();
			AuraComponent spellComponent = DevotionComponents.AURA_COMPONENT.get(player);
			spellComponent.drainAura(5, false);
			return Command.SINGLE_SUCCESS;
		}
	}
}
