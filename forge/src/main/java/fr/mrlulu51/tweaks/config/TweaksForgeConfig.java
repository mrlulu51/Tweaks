package fr.mrlulu51.tweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class TweaksForgeConfig {
    public final ForgeConfigSpec.BooleanValue shulkerViewerEnabled;
    public final ForgeConfigSpec.BooleanValue entityLifeDisplayEnabled;

    public TweaksForgeConfig(ForgeConfigSpec.Builder builder) {
        shulkerViewerEnabled = builder.define("shulkerViewEnabled", true);
        entityLifeDisplayEnabled = builder.define("entityLifeDisplayEnabled", true);
    }

    public static TweaksForgeConfig registerConfigSpecs(ModLoadingContext ctx) {
        var spec = new ForgeConfigSpec.Builder().configure(TweaksForgeConfig::new);
        ctx.registerConfig(ModConfig.Type.CLIENT, spec.getRight());
        return spec.getLeft();
    }
}
