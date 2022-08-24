package com.lukekaalim.mods.interactions.vanilla;

import com.lukekaalim.mods.interactions.BlockTarget;
import com.lukekaalim.mods.interactions.Interaction;

import net.minecraft.item.ItemStack;

public class PlaceInteraction extends Interaction {
  public PlaceInteraction(ItemStack blockItem, BlockTarget blockTarget) {
    super();

    item = blockItem;
    target = blockTarget;
    priority = -1;
  }
}
