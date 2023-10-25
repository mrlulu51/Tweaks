package fr.mrlulu51.tweaks.services;

import fr.mrlulu51.tweaks.FabricTweaks;
import fr.mrlulu51.tweaks.config.TweaksConfigOptions;
import fr.mrlulu51.tweaks.platform.services.IConfigHelper;

public class FabricConfigHelper implements IConfigHelper {
    @Override
    public boolean isShulkerViewerEnabled() {
        return FabricTweaks.config.getOrFallback(TweaksConfigOptions.SHULKER_VIEWER, false);
    }

    @Override
    public boolean isDisplayEntityLifeEnabled() {
        return FabricTweaks.config.getOrFallback(TweaksConfigOptions.ENTITY_LIFE_DISPLAY, false);
    }
}
