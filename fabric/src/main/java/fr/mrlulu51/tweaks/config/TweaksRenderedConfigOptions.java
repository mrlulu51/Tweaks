package fr.mrlulu51.tweaks.config;

import io.github.lgatodu47.catconfigmc.RenderedConfigOption;
import io.github.lgatodu47.catconfigmc.RenderedConfigOptionBuilder;

import java.util.List;

public class TweaksRenderedConfigOptions {

    private static final RenderedConfigOptionBuilder BUILDER = new RenderedConfigOptionBuilder();

    static {
        BUILDER.ofBoolean(TweaksConfigOptions.SHULKER_VIEWER).setCommonTranslationKey("config.shulker_view").build();
        BUILDER.ofBoolean(TweaksConfigOptions.ENTITY_LIFE_DISPLAY).setCommonTranslationKey("config.entity_life_display").build();
    }

    public static List<RenderedConfigOption<?>> options() {
        return BUILDER.optionsToRender();
    }
}
