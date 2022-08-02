package com.lukekaalim.mods.fresh_air.mixin;

import java.util.Dictionary;

import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lukekaalim.mods.fresh_air.FreshAirMod;
import com.lukekaalim.mods.fresh_air.PlayerEntityRenderCallback;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vector4f;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

  RenderLayer buildLayer(Identifier textureIdentifier) {
    var phaseParams = RenderLayer.MultiPhaseParameters
      .builder()
      .texture(new RenderPhase.Texture(textureIdentifier, false, false))
      .shader(RenderLayer.ENTITY_TRANSLUCENT_SHADER)
      .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
      .writeMaskState(new RenderPhase.WriteMaskState(true, false))
      .lightmap(RenderLayer.ENABLE_LIGHTMAP)
      .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
      .build(true);
    
    var renderLayer = RenderLayer.of(
      "entity_translucent",
      VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
      DrawMode.QUADS,
      262144,
      true,
      true,
      phaseParams
    );
    return renderLayer;
  }

  PlayerEntityModel<AbstractClientPlayerEntity> setupModel(AbstractClientPlayerEntity player, PlayerEntityRenderer renderer, float tickDelta, MatrixStack matrices) {
    var playerRendererInvoker = (PlayerEntityRendererInvoker)renderer;
    var livingRendererInvoker = (LivingEntityRendererInvoker)renderer;

    var bodyYaw = player.bodyYaw;
    var headPitch = player.getPitch();
    var animationProgress = (float)player.age + tickDelta;

    float scale = 0.9375f;

    playerRendererInvoker.invokeSetModelPose(player);
    playerRendererInvoker.invokeSetupTransforms(player, matrices, animationProgress, bodyYaw, tickDelta);

    matrices.scale(-1.0f, -1.0f, 1.0f);
    matrices.translate(0.0, -1.501f, 0.0);
    matrices.scale(scale, scale, scale);

    var model = renderer.getModel();

    model.handSwingProgress = livingRendererInvoker.invokeGetHandSwingProgress(player, tickDelta);

    var limbAngle = player.limbAngle;
    var limbDistance = player.limbDistance;
    
    model.child = false;
    model.animateModel(player, limbAngle, limbDistance, tickDelta);
    model.setAngles(player, limbAngle, limbDistance, animationProgress, 0f, headPitch);

    return model;
  }

  Vector4f calculateTint(AbstractClientPlayerEntity player) {
    if (MinecraftClient.getInstance().player == player)
      return new Vector4f(1f, 1f, 1f, 0.25f);
    else
      return new Vector4f(1f, 1f, 1f, 1f);
  }

  public void render(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int lightLevel) {
    matrices.push();
    var renderer = (PlayerEntityRenderer)(Object)this;

    var renderLayer = buildLayer(renderer.getTexture(player));
    var model = setupModel(player, renderer, tickDelta, matrices);

    var vertices = vertexConsumerProvider.getBuffer(renderLayer);
    var overlay = LivingEntityRenderer.getOverlay(player, 0f);
    var tint = calculateTint(player);

    model.render(
      matrices,
      vertices,
      lightLevel,
      overlay,
      tint.getX(), tint.getY(), tint.getZ(), tint.getW()
    );

    matrices.pop();
  }
}
