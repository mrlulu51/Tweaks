package fr.mrlulu51.tweaks.config;

import fr.mrlulu51.tweaks.FabricTweaks;
import io.github.lgatodu47.catconfigmc.RenderedConfigOption;
import io.github.lgatodu47.catconfigmc.RenderedConfigOptionBuilder;

import java.util.List;

public class TweaksRenderedConfigOptions {

    private static final RenderedConfigOptionBuilder BUILDER = new RenderedConfigOptionBuilder();

    static {
        BUILDER.ofBoolean(TweaksConfigOptions.SHULKER_VIEWER).setCommonTranslationKey("config.shulker_view").build();
    }

    public static List<RenderedConfigOption<?>> options() {
        return BUILDER.optionsToRender();
    }
}
