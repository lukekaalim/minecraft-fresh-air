package com.lukekaalim.mods.interactions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/*
 * Use an Item on a Block
 * e.g.
 *  - Strip the bark from a log
 *  - turn dirt into path
 */
public class ItemBlockUseInteraction extends UseInteraction {
  public Block block;
  public BlockPos position;
  public BlockState state;

  public ItemStack item;
}
