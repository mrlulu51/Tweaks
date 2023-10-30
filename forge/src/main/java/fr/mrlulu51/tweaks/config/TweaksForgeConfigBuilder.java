package fr.mrlulu51.tweaks.config;

import com.google.common.collect.ImmutableList;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.function.FailableFunction;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TweaksForgeConfigBuilder {

    private final List<TweaksRenderedConfigOption<?>> options;

    public TweaksForgeConfigBuilder() {
        this(new ArrayList<>());
    }

    public TweaksForgeConfigBuilder(List<TweaksRenderedConfigOption<?>> options) {
        options.clear();
        this.options = options;
    }

    public <V> BuildingRenderedOption<V> option(ForgeConfigSpec.ConfigValue<V> option) {
        return new BuildingRenderedOption<>(option, options::add);
    }

    public BuildingRenderedOption<Boolean> ofBoolean(ForgeConfigSpec.ConfigValue<Boolean> option) {
        return option(option).setWidgetFactory(config -> BuiltinWidgets.createBoolWidget(config, option));
    }

    public BuildingRenderedOption<Integer> ofInt(ForgeConfigSpec.ConfigValue<Integer> option) {
        return option(option).setWidgetFactory(config -> BuiltinWidgets.createIntWidget(config, option));
    }

    public BuildingRenderedOption<Long> ofLong(ForgeConfigSpec.ConfigValue<Long> option) {
        return option(option).setWidgetFactory(config -> BuiltinWidgets.createLongWidget(config, option));
    }

    public BuildingRenderedOption<Double> ofDouble(ForgeConfigSpec.ConfigValue<Double> option) {
        return option(option).setWidgetFactory(config -> BuiltinWidgets.createDoubleWidget(config, option));
    }

    public BuildingRenderedOption<String> ofString(ForgeConfigSpec.ConfigValue<String> option, boolean extendedLength) {
        return option(option).setWidgetFactory(config -> BuiltinWidgets.createStringWidget(config, option, extendedLength));
    }

    public <E extends Enum<E>> BuildingRenderedOption<E> ofEnum(ForgeConfigSpec.ConfigValue<E> option, Class<E> enumClass) {
        return option(option).setWidgetFactory(config -> BuiltinWidgets.createEnumWidget(config, option, enumClass));
    }

    public List<TweaksRenderedConfigOption<?>> optionsToRender() {
        return ImmutableList.copyOf(options);
    }

    public static final class BuildingRenderedOption<V> {
        private final ForgeConfigSpec.ConfigValue<V> option;
        private final Consumer<TweaksRenderedConfigOption<?>> appender;
        private Component name, description;
        private Function<TweaksForgeConfigAccess, AbstractWidget> widgetFactory;

        public BuildingRenderedOption(ForgeConfigSpec.ConfigValue<V> option, Consumer<TweaksRenderedConfigOption<?>> appender) {
            this.option = option;
            this.appender = appender;
        }

        public BuildingRenderedOption<V> setCommonTranslationKey(String translationKey){
            return setName(Component.translatable(translationKey)).setDescription(Component.translatable(translationKey.concat(".desc")));
        }

        public BuildingRenderedOption<V> setName(Component name) {
            this.name = name;
            return this;
        }

        public BuildingRenderedOption<V> setDescription(Component desc) {
            this.description = desc;
            return this;
        }

        public BuildingRenderedOption<V> setWidgetFactory(Function<TweaksForgeConfigAccess, AbstractWidget> widgetFactory) {
            this.widgetFactory = widgetFactory;
            return this;
        }

        public TweaksRenderedConfigOption<V> build() {
            TweaksRenderedConfigOption<V> opt = new RenderedConfigOptionImpl<>(this.option,
                    name == null ? Component.literal(option.getPath().get(option.getPath().size() - 1)) : name,
                    description == null ? Component.empty() : description,
                    widgetFactory == null ? w -> null : widgetFactory);
            appender.accept(opt);
            return opt;
        }

        private record RenderedConfigOptionImpl<V>(ForgeConfigSpec.ConfigValue<V> option, Component title, Component description, Function<TweaksForgeConfigAccess, AbstractWidget> widgetMaker) implements TweaksRenderedConfigOption<V> {
            @Override
            public AbstractWidget createWidget(TweaksForgeConfigAccess config) {
                return widgetMaker().apply(config);
            }
        }
    }

    final class BuiltinWidgets {
        private static final int INT_MAX_DIGITS = numDigits(Integer.MAX_VALUE);
        private static final int LONG_MAX_DIGITS = Long.toString(Long.MAX_VALUE).length();

        static AbstractWidget createBoolWidget(TweaksForgeConfigAccess config, ForgeConfigSpec.ConfigValue<Boolean> option) {
            return new Button(0, 0, 100, 20, Component.empty(), button -> config.put(option, config.get(option).map(b -> !b).orElse(false)), Supplier::get) {
                @Override
                public Component getMessage() {
                    return config.get(option).map(Object::toString).map(Component::nullToEmpty).orElseGet(super::getMessage);
                }
            };
        }

        static AbstractWidget createIntWidget(TweaksForgeConfigAccess config, ForgeConfigSpec.ConfigValue<Integer> option) {
            int space = INT_MAX_DIGITS;
            EditBox widget = createNumberWidget(config, option, Mth.clamp(space * 10, 20, 100), String::valueOf, Integer::parseInt, Math::min, Math::max, false);
            widget.setMaxLength(space);
            return widget;
        }

        static AbstractWidget createLongWidget(TweaksForgeConfigAccess config, ForgeConfigSpec.ConfigValue<Long> option) {
            EditBox widget = createNumberWidget(config, option, 100, String::valueOf, Long::parseLong, Math::min, Math::max, false);
            widget.setMaxLength(LONG_MAX_DIGITS + 1);
            return widget;
        }

        private static final DecimalFormat FORMAT = Util.make(new DecimalFormat("#"), format -> {
            format.setMaximumFractionDigits(8);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setDecimalSeparator('.');
            format.setDecimalFormatSymbols(symbols);
        });

        static AbstractWidget createDoubleWidget(TweaksForgeConfigAccess config, ForgeConfigSpec.ConfigValue<Double> option) {
            EditBox widget = createNumberWidget(config, option, 100, FORMAT::format, Double::parseDouble, Math::min, Math::max, true);
            widget.setMaxLength(64);
            return widget;
        }


        static AbstractWidget createStringWidget(TweaksForgeConfigAccess config, ForgeConfigSpec.ConfigValue<String> option, boolean extendedLength) {
            EditBox widget = new EditBox(Minecraft.getInstance().font, 0, 0, 100, 20, Component.empty());
            widget.setValue(config.getOrFallback(option, ""));
            widget.setMaxLength(extendedLength ? 256 : 64);
            widget.setResponder(s -> config.put(option, s));
            return widget;
        }

        static <E extends Enum<E>> AbstractWidget createEnumWidget(TweaksForgeConfigAccess config, ForgeConfigSpec.ConfigValue<E> option, Class<E> enumClass) {
            CycleButton.Builder<E> builder = CycleButton.builder(e -> Component.literal(e.toString().toUpperCase()));
            builder.withValues(enumClass.getEnumConstants());
            config.get(option).ifPresent(builder::withInitialValue);
            builder.displayOnlyValue();
            return builder.create(0, 0, 100, 20, Component.empty(), (button, value) -> config.put(option, value));
        }

        private static <N extends Number> EditBox createNumberWidget(TweaksForgeConfigAccess config, ForgeConfigSpec.ConfigValue<N> option, int widgetWidth, Function<N, String> toString, FailableFunction<String, N, NumberFormatException> parser, BinaryOperator<N> minFunc, BinaryOperator<N> maxFunc, boolean acceptFloatingPoint) {
            EditBox widget = new EditBox(Minecraft.getInstance().font, 0, 0, widgetWidth, 20, Component.empty());
            widget.setValue(config.get(option).map(toString).orElse(""));
            widget.setFilter(s -> {
                if(s.isEmpty() || s.equals("-") || (acceptFloatingPoint && s.equals("."))) {
                    return true;
                }
                try {
                    parser.apply(s);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
            widget.setResponder(s -> {
                if(s.isEmpty() || s.equals("-") || (acceptFloatingPoint && s.equals("."))) {
                    config.put(option, null);
                    return;
                }
                try {
                    final N parsed = parser.apply(s);
                    N clamped = parsed;
                    if(!Objects.equals(parsed, clamped)) {
                        widget.setValue(toString.apply(clamped));
                    }
                    config.put(option, clamped);
                } catch (NumberFormatException ignored) {
                }
            });
            return widget;
        }

        private static int numDigits(int val) {
            val = Math.abs(val);
            int n = 1;
            if (val >= 100000000) {
                n += 8;
                val /= 100000000;
            }
            if (val >= 10000) {
                n += 4;
                val /= 10000;
            }
            if (val >= 100) {
                n += 2;
                val /= 100;
            }
            if (val >= 10) {
                n += 1;
            }
            return n;
        }
    }
}
