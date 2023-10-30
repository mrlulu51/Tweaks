package fr.mrlulu51.tweaks.config;

import fr.mrlulu51.tweaks.ForgeTweaks;

import java.util.List;

public class TweaksForgeRenderedConfig {

    private static final TweaksForgeConfigBuilder BUILDER = new TweaksForgeConfigBuilder();

    static {
        BUILDER.ofBoolean(ForgeTweaks.config.shulkerViewerEnabled).setCommonTranslationKey("config.shulker_view").build();
        BUILDER.ofBoolean(ForgeTweaks.config.entityLifeDisplayEnabled).setCommonTranslationKey("config.entity_life_display").build();
    }

    public static List<TweaksRenderedConfigOption<?>> options() { return BUILDER.optionsToRender(); }
}
