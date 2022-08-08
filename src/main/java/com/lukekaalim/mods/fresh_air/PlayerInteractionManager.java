package com.lukekaalim.mods.fresh_air;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import mikera.vectorz.Vector3;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PlayerInteractionManager {
  public PlayerEntity player;

  public PlayerInteractionManager(PlayerEntity player) {
    this.player = player;
  }

  public Box CreateInteractionBox() {
    var playerBlockPosition = player.getPos();
    var playerYawDegrees = player.getYaw();
    var playerYawRadians = ((playerYawDegrees+90)/180 * Math.PI);
    var interactionSize = 2;

    var playerFacingDirection = new Vector3(Math.cos(playerYawRadians), interactionSize/2, Math.sin(playerYawRadians));
    var playerCenterVector = new Vector3(playerBlockPosition.getX(), playerBlockPosition.getY(), playerBlockPosition.getZ());


    var interactionOrigin = playerCenterVector.addCopy(playerFacingDirection.multiplyCopy(interactionSize));

    return Box.from(new Vec3d(interactionOrigin.x - 0.5f, interactionOrigin.y - 0.5f, interactionOrigin.z - 0.5f)).expand(interactionSize);
  }
  
  public List<Entity> CalculateInteractableEntities() {
    var box = CreateInteractionBox();
    var interactableEntities = player.world.getOtherEntities(player, box, e -> true);

    return interactableEntities;
  }
  public List<BlockEntity> CalculateInteractableBlockEntities() {
    var box = CreateInteractionBox();
    var blockEntities = BlockPos.stream(box)
      .map(pos -> player.world.getBlockEntity(pos))
      .filter(entity -> entity != null)
      .toList();

    return blockEntities;
  }

  public List<BlockPos> CalculateTargetedBlock() {
    var playerPos = player.getBlockPos();
    var playerYawDegrees = player.getYaw();
    var playerYawRadians = ((playerYawDegrees + 90)/180 * Math.PI);

    var playerFacingDirection = new Vector3(Math.cos(playerYawRadians), 1, Math.sin(playerYawRadians));
    var playerCenterVector = new Vector3(playerPos.getX(), playerPos.getY(), playerPos.getZ());

    var interactionOrigin = playerCenterVector.addCopy(playerFacingDirection);

    var startBlockPos = new BlockPos(Math.round(interactionOrigin.x), Math.round(interactionOrigin.y), Math.round(interactionOrigin.z));

    var upBlockPos = startBlockPos.offset(Direction.UP);
    var downBlockPos = startBlockPos.offset(Direction.DOWN);
    var floorBlockPos = downBlockPos.offset(Direction.DOWN);

    return Arrays
      .stream(new BlockPos[] {
        startBlockPos,
        upBlockPos,
        downBlockPos,
        floorBlockPos
      })
      .filter(pos -> !player.world.getBlockState(pos).isAir())
      .toList();
  }

  public class InteractionList {
    public List<Entity> Entities;
    public List<BlockEntity> BlockEntities;
    public List<BlockPos> Blocks;
  }
}
