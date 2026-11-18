package com.eternalnightmares.entity.BlueWisp;

import com.eternalnightmares.EternalNightmares;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BlueWispRender extends EntityRenderer<BlueWispEntity> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EternalNightmares.MODID, "textures/entity/bluewisp/bluewisp.png");

    public BlueWispRender(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0f;
    }

    @Override
    public ResourceLocation getTextureLocation(BlueWispEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(BlueWispEntity entity, float entityYaw, float partialTick, PoseStack pose, MultiBufferSource buffers, int packedLight) {
        pose.pushPose();
        pose.mulPose(this.entityRenderDispatcher.camera.rotation());
        pose.mulPose(Axis.YP.rotationDegrees(180.0f));

        float halfWidth = 1.0f;
        float height = 2.0f;
        float yOff = 0.02f;

        VertexConsumer vc = buffers.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));

        vc.addVertex(pose.last(), -halfWidth, yOff, 0.0f).setColor(255, 255, 255, 255).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0f, 1.0f, 0.0f);
        vc.addVertex(pose.last(), halfWidth, yOff, 0.0f).setColor(255, 255, 255, 255).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0f, 1.0f, 0.0f);
        vc.addVertex(pose.last(), halfWidth, yOff + height, 0.0f).setColor(255, 255, 255, 255).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0f, 1.0f, 0.0f);
        vc.addVertex(pose.last(), -halfWidth, yOff + height, 0.0f).setColor(255, 255, 255, 255).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0f, 1.0f, 0.0f);

        pose.popPose();

        super.render(entity, entityYaw, partialTick, pose, buffers, packedLight);
    }
}
