package fr.mrlulu51.tweaks.config;

import fr.mrlulu51.tweaks.FabricTweaks;
import io.github.lgatodu47.catconfig.*;
import io.github.lgatodu47.catconfigmc.MinecraftConfigSides;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class TweaksFabricConfig extends CatConfig {
    public TweaksFabricConfig() {
        super(MinecraftConfigSides.CLIENT, "tweaks", CatConfigLogger.delegate(FabricTweaks.LOGGER));
    }

    @Override
    protected @NotNull ConfigOptionAccess getConfigOptions() {
        return TweaksConfigOptions.BUILDER;
    }

    @Override
    protected @NotNull Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
