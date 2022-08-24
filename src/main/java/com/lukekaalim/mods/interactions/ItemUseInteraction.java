package com.lukekaalim.mods.interactions;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/*
 * Use an item in a hand
 * e.g.
 *  - Drink a potion
 *  - Eat some food
 *  - Hold your shield
 */
public class ItemUseInteraction extends UseInteraction {
  public ItemStack item;
  public Hand hand = Hand.MAIN_HAND;
}
