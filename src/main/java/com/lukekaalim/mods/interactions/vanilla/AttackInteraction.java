package com.lukekaalim.mods.interactions.vanilla;

import com.lukekaalim.mods.interactions.EntityTarget;
import com.lukekaalim.mods.interactions.Intention;
import com.lukekaalim.mods.interactions.Interaction;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class AttackInteraction extends Interaction {
  public LivingEntity entityTarget;

  public AttackInteraction(PlayerEntity player, ItemStack weapon, LivingEntity entityTarget) {
    this.actor = player;
    this.item = weapon;
    this.intention = Intention.ATTACK;
    this.target = new EntityTarget(entityTarget);
    this.entityTarget = entityTarget;
  }
}
