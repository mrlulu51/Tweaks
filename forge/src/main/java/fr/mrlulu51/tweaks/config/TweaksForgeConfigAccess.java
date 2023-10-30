package fr.mrlulu51.tweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Optional;
import java.util.function.Supplier;

public interface TweaksForgeConfigAccess {
    <V> void put(ForgeConfigSpec.ConfigValue<V> option, V value);

    <V> Optional<V> get(ForgeConfigSpec.ConfigValue<V> option);

    default <V> V getOrFallback(ForgeConfigSpec.ConfigValue<V> option, V fallback) {
        return get(option).orElse(fallback);
    }

    default <V> V getOrFallback(ForgeConfigSpec.ConfigValue<V> option, Supplier<? extends V> fallbackSupplier) {
        return get(option).orElseGet(fallbackSupplier);
    }
}
