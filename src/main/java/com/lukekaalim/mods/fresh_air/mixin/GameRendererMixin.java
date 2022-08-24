package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lukekaalim.mods.fresh_air.InteractingPlayer;
import com.lukekaalim.mods.fresh_air.PlayerInteractionManager;
import com.lukekaalim.mods.fresh_air.VectorUtils;
import com.lukekaalim.mods.interactions.BlockTarget;
import com.lukekaalim.mods.interactions.EntityTarget;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
  @Inject(
    method = "updateTargetedEntity",
    at = @At("HEAD"),
    cancellable = true
  )
  void updateTargetedEntity(float tickDelta, CallbackInfo ci) {
    var renderer = (GameRenderer)(Object)this;
    var client = renderer.getClient();

    var clientCamera = client.cameraEntity;
    if (clientCamera instanceof InteractingPlayer player) {
      PlayerInteractionManager im = player.getInteractionManager();
      im.Tick();
      var target = im.GetDefaultTarget();
      if (target == null) {
        client.crosshairTarget = BlockHitResult.createMissed(new Vec3d(0, 0, 0), Direction.UP, im.player.getBlockPos());
        client.targetedEntity = null;
      } else {
        client.crosshairTarget = target.Hit();
        if (target instanceof EntityTarget entityTarget) {
          client.targetedEntity = entityTarget.entity;
        } else {
          client.targetedEntity = null;
        }
      }
    }

    ci.cancel();
  }
}
