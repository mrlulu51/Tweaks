package fr.mrlulu51.tweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.mrlulu51.tweaks.Constants;
import fr.mrlulu51.tweaks.platform.Services;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    @Shadow public abstract void render(T $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5);

    @Unique
    private static final ResourceLocation LIFE_INDICATOR = new ResourceLocation(Constants.MOD_ID, "textures/misc/life_indicator.png");
    @Unique
    private static final RenderType TEXTURE_RENDER_TYPE = RenderType.text(LIFE_INDICATOR);
    @Unique
    private static final int[] FULL_HP_RGB = new int[] {66, 216, 49}, LOW_HP_RGB = new int[] {216, 49, 49};
    @Unique
    private static final DecimalFormat LIFE_FORMAT = new DecimalFormat("#.#");

    protected LivingEntityRendererMixin(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    public void render(T entity, float val, float delta, PoseStack pose, MultiBufferSource buff, int light, CallbackInfo ci) {
        if(entity != entityRenderDispatcher.crosshairPickEntity) {
            return;
        }
        double distance = this.entityRenderDispatcher.distanceToSqr(entity);

        if(distance <= 4096 && Services.CONFIG.isDisplayEntityLifeEnabled() && entity != entityRenderDispatcher.camera.getEntity()) {
            boolean notDiscrete = !entity.isDiscrete();
            float nametagOffset = entity.getNameTagOffsetY() + 0.75F;
            float life = entity.getHealth();

            pose.pushPose();
            pose.translate(0.0F, nametagOffset, 0.0F);
            pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
            pose.scale(-0.025F, -0.025F, 0.025F);

            Matrix4f matrix = pose.last().pose();
            float backgroundOpacity = entityRenderDispatcher.options.getBackgroundOpacity(0.25F);
            int alpha = (int) (backgroundOpacity * 255.0F) << 24;
            Component hpComponent = Component.translatable("tweaks.entity_life.name", LIFE_FORMAT.format(life));
            Font font = this.getFont();
            float textX = (float) (-font.width(hpComponent) / 2);

            pose.pushPose();
            pose.translate(textX - 10, 0, 0);
            tweaks_MC$renderLifeIndicator(Mth.clamp(1 - life / entity.getMaxHealth(), 0, 1), pose.last().pose(), buff.getBuffer(TEXTURE_RENDER_TYPE), light);
            pose.popPose();

            font.drawInBatch(hpComponent, textX, 0, 553648127, false, matrix, buff, notDiscrete ? Font.DisplayMode.NORMAL : Font.DisplayMode.SEE_THROUGH, alpha, light);
            if(notDiscrete) {
                font.drawInBatch(hpComponent, textX, 0, -1, false, matrix, buff, Font.DisplayMode.NORMAL, 0, light);
            }

            pose.popPose();
        }
    }

    @Unique
    private void tweaks_MC$renderLifeIndicator(float hpRatio, Matrix4f pose, VertexConsumer consumer, int light) {
        int[] currentHPRGB = new int[] {
                (int) (FULL_HP_RGB[0] - (FULL_HP_RGB[0] - LOW_HP_RGB[0]) * hpRatio),
                (int) (FULL_HP_RGB[1] - (FULL_HP_RGB[1] - LOW_HP_RGB[1]) * hpRatio),
                49
        };
        consumer.vertex(pose, 8, 0, 0).color(currentHPRGB[0], currentHPRGB[1], currentHPRGB[2], 255).uv(0, 0).uv2(light).endVertex();
        consumer.vertex(pose, 0, 0, 0).color(currentHPRGB[0], currentHPRGB[1], currentHPRGB[2], 255).uv(1, 0).uv2(light).endVertex();
        consumer.vertex(pose, 0, 8, 0).color(currentHPRGB[0], currentHPRGB[1], currentHPRGB[2], 255).uv(1, 1).uv2(light).endVertex();
        consumer.vertex(pose, 8, 8, 0).color(currentHPRGB[0], currentHPRGB[1], currentHPRGB[2], 255).uv(0, 1).uv2(light).endVertex();
    }
}
