package fr.mrlulu51.tweaks.client.screen;

import fr.mrlulu51.tweaks.FabricTweaks;
import fr.mrlulu51.tweaks.config.TweaksRenderedConfigOptions;
import io.github.lgatodu47.catconfigmc.screen.ModConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TweaksConfigScreen extends ModConfigScreen {

    public TweaksConfigScreen(Screen parent) {
        super(Component.translatable("options.tweaks.title"), parent, FabricTweaks.config, TweaksRenderedConfigOptions::options);
    }
}
