package com.lukekaalim.mods.interactions;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockTarget extends Target {
  public BlockPos blockPos;
  public BlockState blockState;
  public Block block;
  public BlockEntity entity;
  public World world;

  public BlockTarget(World world, BlockPos blockPos) {
    this.world = world;
    this.blockPos = blockPos;
    this.blockState = world.getBlockState(blockPos);
    this.block = blockState.getBlock();
    this.entity = world.getBlockEntity(blockPos);
  }

  @Override
  public Text Describe() {
    return block.getName();
  }

  @Override
  public Box Bounds() {
    return new Box(blockPos).expand(0.05f);
  }

  @Override
  public HitResult Hit() {
    var blockVec = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    return new BlockHitResult(blockVec.add(new Vec3d(0, 0.5f, 0)), Direction.UP, blockPos, true);
  }
}