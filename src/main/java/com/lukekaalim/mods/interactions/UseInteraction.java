package com.lukekaalim.mods.interactions;

import net.minecraft.item.ItemStack;

/*
 * An object, block, entity or some combination
 * are being used together in an interaction.
 */
public abstract class UseInteraction extends Interaction {
  public Target target;
  public ItemStack item;
}
