package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(PlayerEntityRenderer.class)
public interface PlayerEntityRendererInvoker {
  @Invoker("setupTransforms")
  public void invokeSetupTransforms(AbstractClientPlayerEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);


  @Invoker("setModelPose")
  public void invokeSetModelPose(AbstractClientPlayerEntity entity);
}