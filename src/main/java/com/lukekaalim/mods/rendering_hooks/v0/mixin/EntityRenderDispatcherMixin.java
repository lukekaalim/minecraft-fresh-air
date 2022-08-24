package com.lukekaalim.mods.rendering_hooks.v0.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lukekaalim.mods.fresh_air.FreshAirMod;
import com.lukekaalim.mods.rendering_hooks.v0.EntityRenderContext;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
  @Shadow
  public Camera camera;

  @Inject(method = "render", at = @At(value = "HEAD"))
  public <E extends Entity> void onBeforeRender(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
    var context = new EntityRenderContext();
    context.light = light;
    context.tickDelta = tickDelta;
    context.matrices = matrices;
    context.vertexConsumers = vertexConsumers;
    context.camera = camera;
    FreshAirMod.ENTITY_RENDERER_REGISTRY.RunPreRender((Entity)entity, context);
  }
  @Inject(method = "render", at = @At(value = "TAIL"))
  public <E extends Entity> void onAfterRender(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

  }
}
