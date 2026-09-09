package com.truespace.init;

import com.truespace.TrueSpaceMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Creative tabs for the mod.
 */
public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TrueSpaceMod.MODID);

    /** Main tab. Will be split into Materials / Machines / Propulsion / etc. as content grows. */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.truespace"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.ALUMINUM_INGOT.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Bauxite → aluminium chain, in processing order.
                        output.accept(ModBlocks.BAUXITE_ORE.get());
                        output.accept(ModItems.CRUSHED_BAUXITE.get());
                        output.accept(ModItems.ALUMINA.get());
                        output.accept(ModItems.CRYOLITE.get());
                        output.accept(ModItems.CARBON_ANODE.get());
                        output.accept(ModItems.ALUMINUM_INGOT.get());
                        output.accept(ModItems.RED_MUD.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }
}
