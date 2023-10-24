package fr.mrlulu51.tweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.mrlulu51.tweaks.Constants;
import fr.mrlulu51.tweaks.platform.Services;
import fr.mrlulu51.tweaks.util.NbtParser;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {

    @Shadow @Nullable protected Slot hoveredSlot;
    private final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/tooltips/shulker.png");

    protected AbstractContainerScreenMixin(Component $$0) {
        super($$0);
    }

    @Inject(at = @At("HEAD"), method = "renderTooltip", cancellable = true)
    public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo info) {
        if(hoveredSlot == null) {
            return;
        }
        ItemStack stack = this.hoveredSlot.getItem();
        Block shulker = Block.byItem(stack.getItem());
        System.out.println(shulker);

        if(shulker instanceof ShulkerBoxBlock && Services.CONFIG.isShulkerViewerEnabled()) {
            this.renderShulkerTooltip(graphics, mouseX, mouseY, stack);
            info.cancel();
        }
    }

    private void renderShulkerTooltip(GuiGraphics graphics, int mouseX, int mouseY, ItemStack stack) {
        CompoundTag shulkerTag = stack.getOrCreateTag();
        NonNullList<ItemStack> inventory = shulkerTag.contains("BlockEntityTag")
                ? NbtParser.readInventoryNbt(shulkerTag.getCompound("BlockEntityTag"))
                : NonNullList.withSize(27, ItemStack.EMPTY);
        PoseStack pose = graphics.pose();

        graphics.blit(BACKGROUND_TEXTURE, mouseX + 10, mouseY + 10, 420, 0, 0, 176, 64, 176, 64);

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack contained = inventory.get(i);

            if(!contained.isEmpty()) {
                pose.pushPose();
                pose.translate(0.0f, 0.0f, 500);
                graphics.renderItem(minecraft.player, contained, mouseX + 18 + 18 * (i % 9), mouseY + 16 + 18 * Mth.floor(i / 9.), 0);
                graphics.renderItemDecorations(font, contained, mouseX + 18 + 18 * (i % 9), mouseY + 16 + 18 * Mth.floor(i / 9.));
                pose.popPose();
            }
        }
    }
}
