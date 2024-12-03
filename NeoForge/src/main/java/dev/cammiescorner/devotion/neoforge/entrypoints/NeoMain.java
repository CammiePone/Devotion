package dev.cammiescorner.devotion.neoforge.entrypoints;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.neoforge.common.attachments.entity.AuraAttachment;
import dev.cammiescorner.devotion.neoforge.common.attachments.entity.KnownResearchAttachment;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@Mod(Devotion.MOD_ID)
public class NeoMain {
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Devotion.MOD_ID);

	// TODO make aura copy over only the primary aura type upon dying
	public static final Supplier<AttachmentType<AuraAttachment>> AURA = ATTACHMENT_TYPES.register(
		"aura", () -> AttachmentType.serializable(AuraAttachment::new).copyOnDeath().build()
	);
	public static final Supplier<AttachmentType<KnownResearchAttachment>> KNOWN_RESEARCH = ATTACHMENT_TYPES.register(
		"known_research", () -> AttachmentType.serializable(KnownResearchAttachment::new).copyOnDeath().build()
	);

	public NeoMain(IEventBus modBus) {
		ATTACHMENT_TYPES.register(modBus);
		AuraAttachment.registerAuraProviders(Player.class);
	}
}
