package fr.mrlulu51.tweaks.client.screen.widget;

import com.google.common.collect.Lists;
import fr.mrlulu51.tweaks.client.screen.TweaksConfigScreen;
import fr.mrlulu51.tweaks.config.TweaksForgeConfig;
import fr.mrlulu51.tweaks.config.TweaksForgeConfigAccess;
import fr.mrlulu51.tweaks.config.TweaksRenderedConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;

public class TweaksConfigScreenWidget<E extends TweaksConfigScreenWidget.TweaksConfigOptionEntry<E>> extends ContainerObjectSelectionList<E> implements TweaksConfigScreen.IConfigOptionListWidget {

    public TweaksConfigScreenWidget(Minecraft client, int width, int height, int top, int bottom) {
        super(client, width, height, top, bottom, 36);
    }

    @Override
    public void tick() {
        this.children().forEach(TweaksConfigOptionEntry::tick);
    }

    @Override
    public int getRowLeft() {
        return 0;
    }

    @Override
    public int getRowWidth() {
        return this.width - 6;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width - 6;
    }

    @Override
    public void renderImpl(GuiGraphics context, int mouseX, int mouseY, float delta) {
        render(context, mouseX, mouseY, delta);
    }

    @Override
    public void updateChildElementsClickedState(double mouseX, double mouseY, int button) {
        children().stream().map(child -> child.widget).filter(EditBox.class::isInstance).forEach(field -> field.mouseClicked(mouseX, mouseY, button));
    }

    public void addAll(TweaksForgeConfig config, Supplier<Iterable<TweaksRenderedConfigOption<?>>> renderedOptionsSupplier) {
        renderedOptionsSupplier.get().forEach(option -> {
            AbstractWidget widget = option.createWidget(config);
            if(widget != null) {
                addEntry(makeEntry(option, widget));
            }
        });
    }

    @Override
    public Optional<Component> getHoveredButtonDescription(double mouseX, double mouseY) {
        if(mouseX > this.x0 && mouseX < this.x0 + this.width && mouseY > this.y0 && mouseY < this.y0 + height) {
            for(TweaksConfigOptionEntry<?> entry : this.children()) {
                if(entry.widget.isMouseOver(mouseX, mouseY)) {
                    return Optional.of(entry.option.description());
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public OptionalInt bottom() {
        return OptionalInt.of(this.y1);
    }

    protected E makeEntry(TweaksRenderedConfigOption<?> option, AbstractWidget widget) {
        return (E)new TweaksConfigOptionEntry<>(this.minecraft, option, widget);
    }

    public static class TweaksConfigOptionEntry<E extends TweaksConfigOptionEntry<E>> extends ContainerObjectSelectionList.Entry<E> {
        protected final Minecraft client;
        protected final TweaksRenderedConfigOption<?> option;
        protected final AbstractWidget widget;

        public TweaksConfigOptionEntry(Minecraft client, TweaksRenderedConfigOption<?> option, AbstractWidget widget) {
            this.client = client;
            this.option = option;
            this.widget = widget;
        }

        protected void tick() {
            if(widget instanceof EditBox textField) {
                textField.tick();
            }
        }

        protected float hoveredTime;
        @Override
        public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if(hovered) {
                if(hoveredTime < 1) {
                    hoveredTime = Math.min(1, hoveredTime + 0.1F);
                }
            }
            else {
                if(hoveredTime > 0) {
                    hoveredTime = Math.max(0, hoveredTime - 0.1F);
                }
            }
            context.fill(x, y, x + entryWidth, y + entryHeight, FastColor.ARGB32.color((int) (hoveredTime * 0.2 * 255), 65, 65, 65));
            final int spacing = 8;
            context.drawString(client.font, option.title(), spacing, y + (entryHeight - client.font.lineHeight) / 2, 0xFFFFFF);
            widget.setX(x + entryWidth - spacing - widget.getWidth());
            widget.setY(y + (entryHeight - widget.getHeight()) / 2);
            widget.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Lists.newArrayList(widget);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Lists.newArrayList(widget);
        }
    }
}
