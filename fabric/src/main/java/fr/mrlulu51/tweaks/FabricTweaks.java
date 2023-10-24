package fr.mrlulu51.tweaks;

import fr.mrlulu51.tweaks.config.TweaksFabricConfig;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricTweaks implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static TweaksFabricConfig config;

    @Override
    public void onInitialize() {
        CommonClass.init();
        config = new TweaksFabricConfig();
    }
}
