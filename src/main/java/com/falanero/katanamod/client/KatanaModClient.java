package com.falanero.katanamod.client;

import com.falanero.katanamod.registry.Instances;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class KatanaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Instances.FEATHERBLADE_ENTITY, FlyingItemEntityRenderer::new);
    }
}
