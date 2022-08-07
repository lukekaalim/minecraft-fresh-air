package com.lukekaalim.mods.fresh_air;

import java.util.List;

import mikera.vectorz.Vector2;
import mikera.vectorz.Vector3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.message.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
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

  public class InteractionList {
    public List<Entity> Entities;
  }
}
