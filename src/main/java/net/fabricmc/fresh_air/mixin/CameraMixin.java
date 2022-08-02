package net.fabricmc.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fresh_air.CameraOrbiter;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;

@Mixin(Camera.class)
public class CameraMixin implements CameraOrbiter {
  public float CAMERA_DISTANCE = 4.0f;

  @Override
  public void pushCameraDistance(float distance) {
    CAMERA_DISTANCE += distance;
  }

  public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
    var camera = (Camera)(Object)this;

    camera.ready = true;
    camera.area = area;
    camera.focusedEntity = focusedEntity;
    camera.thirdPerson = thirdPerson;
    camera.setRotation(focusedEntity.getYaw(tickDelta), focusedEntity.getPitch(tickDelta));
    camera.setPos(
      MathHelper.lerp(
        (double)tickDelta,
        focusedEntity.prevX,
        focusedEntity.getX()
      ),
      MathHelper.lerp(
        (double)tickDelta,
        focusedEntity.prevY,
        focusedEntity.getY()
      ) + (double)MathHelper.lerp(
        tickDelta,
        camera.lastCameraY,
        camera.cameraY
      ),
      MathHelper.lerp((double)tickDelta, focusedEntity.prevZ, focusedEntity.getZ()));
    if (thirdPerson) {
        if (inverseView) {
          camera.setRotation(camera.yaw + 180.0f, -camera.pitch);
        }
        camera.moveBy(-CAMERA_DISTANCE, 0.0, 0.0);
    } else if (focusedEntity instanceof LivingEntity && ((LivingEntity)focusedEntity).isSleeping()) {
        Direction direction = ((LivingEntity)focusedEntity).getSleepingDirection();
        camera.setRotation(direction != null ? direction.asRotation() - 180.0f : 0.0f, 0.0f);
        camera.moveBy(0.0, 0.3, 0.0);
    }
  }
}
