package fr.mrlulu51.tweaks.client.util;


import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class TweaksKeybindings {

    public KeyMapping openConfig;

    public TweaksKeybindings() {}

    public void registerKeybindings() {
        openConfig = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.tweaks.open_config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.tweaks"
        ));
    }
}
