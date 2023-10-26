package fr.mrlulu51.tweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.mrlulu51.tweaks.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    protected LivingEntityRendererMixin(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    public void render(T entity, float val, float delta, PoseStack pose, MultiBufferSource buff, int light, CallbackInfo ci) {
        double distance = this.entityRenderDispatcher.distanceToSqr(entity);

        if(distance <= 4096 && Services.CONFIG.isDisplayEntityLifeEnabled() && entity != Minecraft.getInstance().player) {
            boolean discrete = !entity.isDiscrete();
            float nametagOffset = entity.getNameTagOffsetY() + 0.75F;
            float life = entity.getHealth();

            pose.pushPose();
            pose.translate(0.0F, nametagOffset, 0.0F);
            pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
            pose.scale(-0.025F, -0.025F, 0.025F);

            Matrix4f matrix = pose.last().pose();
            float backgroundOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int alpha = (int)(backgroundOpacity * 255.0F) << 24;
            Component hpComponent = Component.translatable("tweaks.entity_life.name", (int) life);
            Font font = this.getFont();
            float width = (float)(-font.width(hpComponent) / 2);
            font.drawInBatch(hpComponent, width, 0, 553648127, false, matrix, buff, discrete ? Font.DisplayMode.NORMAL : Font.DisplayMode.SEE_THROUGH, alpha, light);

            if(discrete) {
                font.drawInBatch(hpComponent, width, 0, -1, false, matrix, buff, Font.DisplayMode.NORMAL, 0, light);
            }

            pose.popPose();
        }
    }
}
