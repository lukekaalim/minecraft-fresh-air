package com.lukekaalim.mods.interactions;

import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;

public abstract class Target {
  public abstract Text Describe();
  public abstract Box Bounds();
  public abstract HitResult Hit();
}
