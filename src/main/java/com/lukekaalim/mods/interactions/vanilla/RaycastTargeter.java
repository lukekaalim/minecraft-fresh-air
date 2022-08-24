package com.lukekaalim.mods.interactions.vanilla;

import com.lukekaalim.mods.interactions.Interaction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class RaycastTargeter {
  public class Result {
    public Interaction attack;
    public Interaction use;

    public HitResult hit;
  }

  public Result GetInteractionForEntityHit(EntityHitResult hit, PlayerEntity player) {
    return null;
  }

  public EntityHitResult RaycastEntities() {
    /*
    Vec3d vec3d2 = entity2.getRotationVec(1.0f);
    Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
    float f = 1.0f;
    Box box = entity2.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
    EntityHitResult entityHitResult = ProjectileUtil.raycast(entity2, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), e);
    if (entityHitResult != null) {
        Entity entity22 = entityHitResult.getEntity();
        Vec3d vec3d4 = entityHitResult.getPos();
        double g = vec3d.squaredDistanceTo(vec3d4);
        if (bl && g > 9.0) {
            this.client.crosshairTarget = BlockHitResult.createMissed(vec3d4, Direction.getFacing(vec3d2.x, vec3d2.y, vec3d2.z), new BlockPos(vec3d4));
        } else if (g < e || this.client.crosshairTarget == null) {
            this.client.crosshairTarget = entityHitResult;
            if (entity22 instanceof LivingEntity || entity22 instanceof ItemFrameEntity) {
                this.client.targetedEntity = entity22;
            }
        }
    }
    */
    return null;
  }

  public Result GetPlayerInteraction(PlayerEntity player) {
    return null;
  }
}
