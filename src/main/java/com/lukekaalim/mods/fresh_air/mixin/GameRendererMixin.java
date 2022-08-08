package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lukekaalim.mods.fresh_air.InteractingPlayer;
import com.lukekaalim.mods.fresh_air.PlayerInteractionManager;

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
      var entities = im.CalculateInteractableEntities();
      var blocks = im.CalculateTargetedBlock();
      if (entities.size() > 0) {
        var firstEntity = entities.get(0);
        client.crosshairTarget = new EntityHitResult(firstEntity);
        client.targetedEntity = firstEntity;
      } else {
        client.targetedEntity = null;
        if (blocks.size() > 0) {
          var blockPos = blocks.get(0);
          var blockVec = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
          client.crosshairTarget = new BlockHitResult(blockVec, Direction.UP, blockPos, true);

        } else {
          client.crosshairTarget = BlockHitResult.createMissed(new Vec3d(0, 0, 0), Direction.UP, im.player.getBlockPos());
        }
      }
    }

    ci.cancel();
  }
}
