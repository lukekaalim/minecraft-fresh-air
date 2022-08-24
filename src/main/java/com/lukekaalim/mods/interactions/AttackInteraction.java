package com.lukekaalim.mods.interactions;

import net.minecraft.item.ItemStack;

/*
 * Absttract interface for Attacking somthing. i.e left clicking
 */
public abstract class AttackInteraction extends Interaction {
  public ItemStack weapon;
  public Target target;
}
