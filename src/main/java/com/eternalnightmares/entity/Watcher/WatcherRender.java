package com.eternalnightmares.entity.Watcher;

import com.eternalnightmares.EternalNightmares;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WatcherRender extends HumanoidMobRenderer<WatcherEntity, HumanoidModel<WatcherEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(EternalNightmares.MODID, "textures/entity/watcher.png");

    public WatcherRender(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(WatcherEntity entity) {
        return TEXTURE;
    }

}
