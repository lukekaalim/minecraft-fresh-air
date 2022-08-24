package com.lukekaalim.mods.fresh_air;

import mikera.vectorz.Vector3;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class VectorUtils {
  public static Vec3d FromBlockPos(BlockPos blockPos) {
    return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }
  public static Vec3d FromBlockPosCenter(BlockPos blockPos) {
    return new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
  }
  public static Vec3d Lerp(Vec3d start, Vec3d end, float t) {
    return new Vec3d(
      MathHelper.lerp(t, start.x, end.x),
      MathHelper.lerp(t, start.y, end.y),
      MathHelper.lerp(t, start.z, end.z)
    );
  }
  public static Vec3d FromVectorz(Vector3 vector) {
    return new Vec3d(vector.x, vector.y, vector.z);
  }
}
