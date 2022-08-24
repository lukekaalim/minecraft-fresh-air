package com.lukekaalim.mods.interactions.vanilla;

import com.lukekaalim.mods.interactions.BlockTarget;
import com.lukekaalim.mods.interactions.Intention;
import com.lukekaalim.mods.interactions.Interaction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class MineInteraction extends Interaction {
  public BlockTarget blockTarget;

  public MineInteraction(PlayerEntity player, ItemStack item, BlockTarget blockTarget) {
    this.intention = Intention.ATTACK;
    this.target = blockTarget;
    this.blockTarget = blockTarget;
    this.item = item;
    this.actor = player;
  }
}
