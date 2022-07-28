package net.fabricmc.example.mixin;

import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.example.ExampleMod;
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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Mixin(PlayerEntityRenderer.class)
public class NiceThirdPersonMixin {
  public void render(AbstractClientPlayerEntity player, float f, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int lightLevel) {
    matrices.push();

    var renderer = (PlayerEntityRenderer)(Object)this;
    var playerRendererInvoker = (PlayerEntityRendererInvoker)renderer;
    var livingRendererInvoker = (LivingEntityRendererInvoker)renderer;
    var bodyYaw = player.bodyYaw;
    var headPitch = player.getPitch();

    var identifier = renderer.getTexture(player);
    
    var phaseParams = RenderLayer.MultiPhaseParameters
      .builder()
      .texture(new RenderPhase.Texture(identifier, false, false))
      .shader(RenderLayer.ENTITY_TRANSLUCENT_SHADER)
      .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
      .writeMaskState(new RenderPhase.WriteMaskState(true, false))
      .lightmap(RenderLayer.ENABLE_LIGHTMAP)
      .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
      .build(true);
    
    var renderLayer = RenderLayer.of(
      "translucent",
      VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
      DrawMode.QUADS,
      262144,
      true,
      true,
      phaseParams
    );
    
    var animationProgress = (float)player.age + tickDelta;

    var vertices = vertexConsumerProvider.getBuffer(renderLayer);
    var model = renderer.getModel();

    playerRendererInvoker.invokeSetModelPose(player);
    model.handSwingProgress = livingRendererInvoker.invokeGetHandSwingProgress(player, tickDelta);
    playerRendererInvoker.invokeSetupTransforms(player, matrices, animationProgress, bodyYaw, tickDelta);

    float scale = 0.9375f;

    matrices.scale(-1.0f, -1.0f, 1.0f);
    matrices.translate(0.0, -1.501f, 0.0);
    matrices.scale(scale, scale, scale);

    var limbAngle = player.limbAngle;
    var limbDistance = player.limbDistance;
    var overlay = LivingEntityRenderer.getOverlay(player, 0f);

    model.child = false;
    model.animateModel(player, limbAngle, limbDistance, tickDelta);
    model.setAngles(player, limbAngle, limbDistance, animationProgress, 0f, headPitch);
    model.render(matrices, vertices, lightLevel, overlay, 1, 1, 1, 0.25f);

    matrices.pop();
  }
}
