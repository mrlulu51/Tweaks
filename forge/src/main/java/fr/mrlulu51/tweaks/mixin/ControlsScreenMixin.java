package fr.mrlulu51.tweaks.mixin;

import fr.mrlulu51.tweaks.ForgeTweaks;
import fr.mrlulu51.tweaks.client.screen.TweaksConfigScreen;
import fr.mrlulu51.tweaks.config.TweaksForgeRenderedConfig;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public class ControlsScreenMixin extends OptionsSubScreen {

    public ControlsScreenMixin(Screen screen, Options options, Component component) {
        super(screen, options, component);
    }

    @Inject(at = @At("HEAD"), method = "init")
    protected void init(CallbackInfo ci) {
        this.addRenderableWidget(Button.builder(Component.translatable("options.tweaks"), (button) -> {
            this.minecraft.setScreen(new TweaksConfigScreen(Component.translatable("options.tweaks.title"), this, ForgeTweaks.config, TweaksForgeRenderedConfig::options));
        }).bounds(this.width / 2 - 100, this.height - 25, 200, 20).build());
    }
}
