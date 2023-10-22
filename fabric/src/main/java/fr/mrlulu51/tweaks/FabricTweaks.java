package fr.mrlulu51.tweaks;

import net.fabricmc.api.ModInitializer;

public class FabricTweaks implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
    }
}
