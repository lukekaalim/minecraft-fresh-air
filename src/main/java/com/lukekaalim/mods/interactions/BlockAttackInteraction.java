package com.lukekaalim.mods.interactions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

/*
 * Attack a block
 * e.g.
 *  - Mine with a pickaxe
 *  - Break some leaves
 */
public class BlockAttackInteraction extends AttackInteraction {
  public Block block;
  public BlockPos position;
  public BlockState state;
}
