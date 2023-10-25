package fr.mrlulu51.tweaks.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.mrlulu51.tweaks.client.screen.TweaksConfigScreen;

public class TweaksModMenuAPI implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return TweaksConfigScreen::new;
    }
}
