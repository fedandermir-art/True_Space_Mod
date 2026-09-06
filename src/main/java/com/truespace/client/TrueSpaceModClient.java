package com.truespace.client;

import com.truespace.TrueSpaceMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * Client-only entry point. Never loaded on a dedicated server.
 * Client rendering, keybinds, HUD (life support / radiation overlays) live here.
 */
@Mod(value = TrueSpaceMod.MODID, dist = Dist.CLIENT)
public class TrueSpaceModClient {

    public TrueSpaceModClient(ModContainer container) {
        // Config screen in the Mods menu -> our mod -> Config.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    // TODO(milestone 3+): HUD for O2/CO2/pressure/temperature/radiation,
    // skybox and space rendering, keybinds.
}
