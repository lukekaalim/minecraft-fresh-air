package com.lukekaalim.mods.interactions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

/*
 * Use a block
 * e.g.
 *  - Use a crafting bench
 *  - Open a door
 */
public class BlockUseInteraction extends UseInteraction {
  public Block block;
  public BlockPos position;
  public BlockState state;
}
