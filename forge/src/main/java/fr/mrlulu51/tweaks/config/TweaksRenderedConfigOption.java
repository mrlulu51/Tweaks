package fr.mrlulu51.tweaks.config;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

public interface TweaksRenderedConfigOption<V> {

    ForgeConfigSpec.ConfigValue<V> option();

    Component title();

    Component description();

    AbstractWidget createWidget(TweaksForgeConfigAccess config);
}
