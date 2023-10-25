package fr.mrlulu51.tweaks.client;

import fr.mrlulu51.tweaks.client.screen.TweaksConfigScreen;
import fr.mrlulu51.tweaks.client.util.TweaksKeybindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

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
                client.setScreen(new TweaksConfigScreen(client.screen));
            }
        });
    }
}
