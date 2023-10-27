package fr.mrlulu51.tweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashMap;
import java.util.Map;

public class TweaksForgeConfig {
    public final ForgeConfigSpec.BooleanValue shulkerViewerEnabled;
    public final ForgeConfigSpec.BooleanValue entityLifeDisplayEnabled;

    public final Map<String, ForgeConfigSpec.ConfigValue<?>> configValues = new HashMap<>();
    public final ForgeConfigSpec.Builder builder;

    public TweaksForgeConfig(ForgeConfigSpec.Builder builder) {
        this.builder = builder;

        this.shulkerViewerEnabled = this.addBooleanConfigValue("shulker_view", true);
        this.entityLifeDisplayEnabled = this.addBooleanConfigValue("entity_life_display", true);
    }

    public static TweaksForgeConfig registerConfigSpecs(ModLoadingContext ctx) {
        var spec = new ForgeConfigSpec.Builder().configure(TweaksForgeConfig::new);
        ctx.registerConfig(ModConfig.Type.CLIENT, spec.getRight());
        return spec.getLeft();
    }

    public <C extends ForgeConfigSpec.ConfigValue<Boolean>> C addBooleanConfigValue(String path, boolean value) {
        C configType = (C) this.builder.define(path, value);
        configValues.put(path, configType);
        return configType;
    }
}
