package fr.mrlulu51.tweaks.client.config;

import fr.mrlulu51.tweaks.ForgeTweaks;
import fr.mrlulu51.tweaks.client.screen.TweaksConfigScreen;
import fr.mrlulu51.tweaks.config.TweaksForgeRenderedConfig;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;

public class TweaksConfigFactoryHandler {

    public static void registerTweaksConfig() {
        ModList.get().getModContainerById("tweaks").ifPresent(container -> {
            container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                    new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new TweaksConfigScreen(
                            Component.translatable("options.tweaks.title"),
                            screen,
                            ForgeTweaks.config,
                            TweaksForgeRenderedConfig::options
                    )));
        });
    }

    public static void registerMinecraftConfig() {
        ModList.get().getModContainerById("minecraft").ifPresent(container -> {
            container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                    new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new OptionsScreen(screen, mc.options)));
        });
    }
}
