package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lukekaalim.mods.fresh_air.WorldRenderable;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
  @Shadow
  BufferBuilderStorage bufferBuilders;

  @Inject(method = "render", at = @At(value = "HEAD"))
  void render(
    MatrixStack matrices,
    float tickDelta,
    long limitTime,
    boolean renderBlockOutline,
    Camera camera,
    GameRenderer gameRenderer,
    LightmapTextureManager lightmapTextureManager,
    Matrix4f positionMatrix,
    CallbackInfo ci
  ) {
    var context = new WorldRenderable.WorldRenderContext();
    context.outlineVertexProvider = bufferBuilders.getOutlineVertexConsumers();
    context.vertexProvider = bufferBuilders.getEntityVertexConsumers();
    context.matrices = matrices;
    context.camera = camera;
    context.gameRenderer = gameRenderer;
    context.tickDelta = tickDelta;

    var cameraPos = camera.getPos();
    
    
    matrices.push();
    matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    
    for (var entry : WorldRenderable.RENDERABLES.entrySet()) {
      var renderable = entry.getValue();
      renderable.PrepareRender(context);
    }

    matrices.pop();
  }
}
