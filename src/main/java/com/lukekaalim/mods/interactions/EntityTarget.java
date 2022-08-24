package com.lukekaalim.mods.interactions;

import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;

public class EntityTarget extends Target {
  public Entity entity;
  
  public EntityTarget(Entity entity) {
    this.entity = entity;
  }

  @Override
  public Text Describe() {
    return entity.getName();
  }

  @Override
  public Box Bounds() {
    return entity.getVisibilityBoundingBox();
  }

  @Override
  public HitResult Hit() {
    return new EntityHitResult(entity);
  }
}