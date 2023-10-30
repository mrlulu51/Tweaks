package fr.mrlulu51.tweaks.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import fr.mrlulu51.tweaks.client.screen.widget.TweaksConfigScreenWidget;
import fr.mrlulu51.tweaks.config.TweaksForgeConfig;
import fr.mrlulu51.tweaks.config.TweaksRenderedConfigOption;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;

public class TweaksConfigScreen extends Screen {

    protected final Screen parent;
    protected final TweaksForgeConfig config;
    protected final Supplier<Iterable<TweaksRenderedConfigOption<?>>> renderedOptionsSupplier;

    protected IConfigOptionListWidget list = IConfigOptionListWidget.NONE;

    public TweaksConfigScreen(Component title, Screen screen, TweaksForgeConfig config, Supplier<Iterable<TweaksRenderedConfigOption<?>>> renderedOptionsSupplier) {
        super(title);
        this.parent = screen;
        this.config = config;
        this.renderedOptionsSupplier = renderedOptionsSupplier;
    }

    @Override
    public void tick() { list.tick(); }

    @Override
    protected void init() {
        final int spacing = 8;
        final int btnHeight = 20;
        final int btnWidth = 200;

        TweaksConfigScreenWidget<?> listWidget = new TweaksConfigScreenWidget<>(this.minecraft, this.width, this.height - spacing * 5 - btnHeight, spacing * 3, this.height - spacing * 2 - btnHeight);
        listWidget.addAll(this.config, this.renderedOptionsSupplier);
        this.list = listWidget;

        this.addWidget(listWidget);
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds((this.width - btnWidth) / 2, this.height - btnHeight - spacing, btnWidth, btnHeight)
                .build());
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderDirtBackground(context);
        list.renderImpl(context, mouseX, mouseY, delta);
        list.bottom().ifPresent(this::renderAboveList);
        context.drawCenteredString(font, title, this.width / 2, 8, 0XFFFFFF);
        context.pose().translate(0, 0, 2);
        super.render(context, mouseX, mouseY, delta);
        context.pose().translate(0, 0, -2);
        list.getHoveredButtonDescription(mouseX, mouseY).ifPresent(desc -> context.renderTooltip(font, desc, mouseX, mouseY));
    }

    protected void renderAboveList(int listBottom) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        builder.vertex(0.0, this.height, 1).uv(0.0f, (float) (listBottom - this.height) / 32.0f).color(64, 64, 64, 255).endVertex();
        builder.vertex(this.width, this.height, 1).uv((float) this.width / 32.0f, (float) (listBottom - this.height) / 32.0f).color(64, 64, 64, 255).endVertex();
        builder.vertex(this.width, listBottom, 1).uv((float) this.width / 32.0f, 0).color(64, 64, 64, 255).endVertex();
        builder.vertex(0.0, listBottom, 1).uv(0.0f, 0).color(64, 64, 64, 255).endVertex();

        tesselator.end();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void removed() {
        TweaksForgeConfig.getConfigSpec().save();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        list.updateChildElementsClickedState(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public interface IConfigOptionListWidget {
        IConfigOptionListWidget NONE = new IConfigOptionListWidget() {
            @Override
            public void tick() {
            }

            @Override
            public void renderImpl(GuiGraphics context, int mouseX, int mouseY, float delta) {
            }

            @Override
            public void updateChildElementsClickedState(double mouseX, double mouseY, int button) {
            }

            @Override
            public Optional<Component> getHoveredButtonDescription(double mouseX, double mouseY) {
                return Optional.empty();
            }

            @Override
            public OptionalInt bottom() {
                return OptionalInt.empty();
            }
        };

        void tick();

        void renderImpl(GuiGraphics context, int mouseX, int mouseY, float delta);

        void updateChildElementsClickedState(double mouseX, double mouseY, int button);

        Optional<Component> getHoveredButtonDescription(double mouseX, double mouseY);

        OptionalInt bottom();
    }
}
