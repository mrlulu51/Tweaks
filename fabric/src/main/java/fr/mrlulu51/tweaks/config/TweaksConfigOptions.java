package fr.mrlulu51.tweaks.config;

import io.github.lgatodu47.catconfig.ConfigOption;
import io.github.lgatodu47.catconfig.ConfigOptionBuilder;
import io.github.lgatodu47.catconfigmc.MinecraftConfigSides;

public class TweaksConfigOptions {
    public static final ConfigOptionBuilder BUILDER = ConfigOptionBuilder.create();

    static {
        BUILDER.onSides(MinecraftConfigSides.CLIENT);
    }
    public static final ConfigOption<Boolean> SHULKER_VIEWER = BUILDER.createBool("shulker_viewer", true);
    public static final ConfigOption<Boolean> ENTITY_LIFE_DISPLAY = BUILDER.createBool("entity_life_display", true);
}
