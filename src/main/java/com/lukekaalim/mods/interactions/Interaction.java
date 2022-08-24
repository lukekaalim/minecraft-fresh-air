package com.lukekaalim.mods.interactions;

import javax.swing.text.html.parser.Entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/*
 * Any kind of interactions involving an "actor" doing something.
 */
public class Interaction {
  public PlayerEntity actor;
  public ItemStack item;
  public Target target;
  public Intention intention;
  public int priority = 0;

  public Text Describe() {
    var intentionString = intention == Intention.ATTACK ? "Attack" : "Use";

    return Text.of(
      intentionString +
      (item != null ? (" " + item.getName().getString() + " on ") : " ") +
      (target != null ? target.Describe().getString() : "")
    );
  }
}
