package fr.mrlulu51.tweaks;

import fr.mrlulu51.tweaks.client.config.TweaksConfigFactoryHandler;
import fr.mrlulu51.tweaks.config.TweaksForgeConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgeTweaks {

    public static TweaksForgeConfig config;
    
    public ForgeTweaks() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onClientSetup);

        config = TweaksForgeConfig.registerConfigSpecs(ModLoadingContext.get());
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        TweaksConfigFactoryHandler.registerMinecraftConfig();
        TweaksConfigFactoryHandler.registerTweaksConfig();
    }
}