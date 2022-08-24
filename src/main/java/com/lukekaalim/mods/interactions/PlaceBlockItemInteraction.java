package com.lukekaalim.mods.interactions;

import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

/*
 * The specific interaction of placing a block in the world
 */
public class PlaceBlockItemInteraction extends ItemUseInteraction {
  public BlockItem blockItem;
  public BlockPos position;
}
