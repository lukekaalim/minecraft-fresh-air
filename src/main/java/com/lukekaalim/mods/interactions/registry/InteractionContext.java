package com.lukekaalim.mods.interactions.registry;

import com.lukekaalim.mods.interactions.Intention;
import com.lukekaalim.mods.interactions.Target;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class InteractionContext {
  public PlayerEntity player;
  public ItemStack item;
  public Target target;
  public Intention intention;

  public InteractionContext(PlayerEntity player, ItemStack item, Target target, Intention intention) {
    this.player = player;
    this.item = item;
    this.target = target;
    this.intention = intention;
  }
}
