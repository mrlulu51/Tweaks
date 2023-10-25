package fr.mrlulu51.tweaks.client;

import fr.mrlulu51.tweaks.FabricTweaks;
import fr.mrlulu51.tweaks.client.util.TweaksKeybindings;
import fr.mrlulu51.tweaks.config.TweaksRenderedConfigOptions;
import io.github.lgatodu47.catconfigmc.screen.ModConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;

public class TweaksClientEntrypoint implements ClientModInitializer {

    public static TweaksKeybindings keys = new TweaksKeybindings();

    @Override
    public void onInitializeClient() {
        keys.registerKeybindings();

        handleKeys();
    }

    private void handleKeys() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keys.openConfig.isDown()) {
                client.setScreen(new ModConfigScreen(Component.translatable("options.tweaks.title"), client.screen, FabricTweaks.config, TweaksRenderedConfigOptions::options));
            }
        });
    }
}
