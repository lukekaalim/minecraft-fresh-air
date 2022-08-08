package com.lukekaalim.mods.fresh_air;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class VectorUtils {
  public static Vec3d FromBlockPos(BlockPos blockPos) {
    return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }
  public static Vec3d FromBlockPosCenter(BlockPos blockPos) {
    return new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
  }
}
