package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.SpellFocus;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.upcraft.sparkweave.api.item.CreativeTabHelper;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DevotionCreativeTabs {
	public static final RegistryHandler<CreativeModeTab> CREATIVE_TABS = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, Devotion.MOD_ID);

	public static final RegistrySupplier<CreativeModeTab> BASE_TAB = CREATIVE_TABS.register("base_tab", () -> CreativeTabHelper.newBuilder(Devotion.id("base_tab")).icon(() -> new ItemStack(DevotionItems.BASIC_MAGE_HOOD.get())).displayItems((parameters, output) -> CreativeTabHelper.addRegistryEntries(parameters, output, DevotionItems.ITEMS)).build());
	public static final RegistrySupplier<CreativeModeTab> STAFF_TAB = CREATIVE_TABS.register("staff_tab", () -> CreativeTabHelper.newBuilder(Devotion.id("staff_tab")).icon(() -> new ItemStack(DevotionItems.STAFF.get())).displayItems((parameters, output) -> {
		for(Holder.Reference<StaffCore> core : DevotionStaffCores.REGISTRY.holders().toList()) {
			ItemStack stack = new ItemStack(DevotionItems.STAFF_CORE.get());
			stack.set(DevotionData.STAFF_CORE.get(), core);
			output.accept(stack);
		}

		for(Holder.Reference<StaffCap> cap : DevotionStaffCaps.REGISTRY.holders().toList()) {
			ItemStack stack = new ItemStack(DevotionItems.STAFF_CAP.get());
			stack.set(DevotionData.STAFF_CAP.get(), cap);
			output.accept(stack);
		}

		for(Holder.Reference<StaffCore> core : DevotionStaffCores.REGISTRY.holders().toList()) {
			for(Holder.Reference<StaffCap> cap : DevotionStaffCaps.REGISTRY.holders().toList()) {
				if(cap.value().inert())
					continue;

				ItemStack stack = new ItemStack(DevotionItems.STAFF.get());
				stack.set(DevotionData.STAFF_CORE.get(), core);
				stack.set(DevotionData.STAFF_CAP.get(), cap);
				output.accept(stack);
			}
		}
	}).build());
	public static final RegistrySupplier<CreativeModeTab> FOCUS_TAB = CREATIVE_TABS.register("focus_tab", () -> CreativeTabHelper.newBuilder(Devotion.id("focus_tab")).icon(() -> new ItemStack(DevotionItems.SPELL_FOCUS.get())).displayItems((parameters, output) -> {
		for(Holder.Reference<SpellFocus> spellFocus : DevotionSpellFoci.REGISTRY.holders().toList()) {
			if(spellFocus.is(DevotionSpellFoci.BLANK.holder()))
				continue;

			ItemStack stack = new ItemStack(DevotionItems.SPELL_FOCUS.get());
			stack.set(DevotionData.SPELL_FOCUS.get(), spellFocus);
			output.accept(stack);
		}
	}).build());
}
