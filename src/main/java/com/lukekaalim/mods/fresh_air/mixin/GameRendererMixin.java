package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lukekaalim.mods.fresh_air.FreshAirMod;
import com.lukekaalim.mods.fresh_air.InteractingPlayer;
import com.lukekaalim.mods.fresh_air.PlayerInteractionManager;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
  /*
  @Redirect(
    method = "updateTargetedEntity",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"
    )
  )
  HitResult selectClientBlockCrosshairTarget(Entity entity, double distance, float tickDelta, boolean includeFluids) {
    var position = entity.getBlockPos();
    var direction = entity.getHorizontalFacing();
    var blockPosInFront = position.offset(direction, 1);

    return new BlockHitResult(
      new Vec3d(blockPosInFront.getX(), blockPosInFront.getY(), blockPosInFront.getZ()),
      Direction.UP,
      blockPosInFront,
      true
    );
  }
  */

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
      if (entities.size() > 0) {
        var firstEntity = entities.get(0);
        client.crosshairTarget = new EntityHitResult(firstEntity);
        client.targetedEntity = firstEntity;
      } else {
        client.targetedEntity = null;
        client.crosshairTarget = BlockHitResult.createMissed(new Vec3d(0, 0, 0), Direction.UP, im.player.getBlockPos());
      }
    }

    ci.cancel();
  }
}
