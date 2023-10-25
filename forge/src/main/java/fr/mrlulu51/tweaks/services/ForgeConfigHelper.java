package fr.mrlulu51.tweaks.services;

import fr.mrlulu51.tweaks.ForgeTweaks;
import fr.mrlulu51.tweaks.platform.services.IConfigHelper;

public class ForgeConfigHelper implements IConfigHelper {

    @Override
    public boolean isShulkerViewerEnabled() {
        return ForgeTweaks.config.shulkerViewerEnabled.get();
    }

    @Override
    public boolean isDisplayEntityLifeEnabled() {
        return ForgeTweaks.config.entityLifeDisplayEnabled.get();
    }
}
