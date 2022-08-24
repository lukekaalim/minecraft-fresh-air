package com.lukekaalim.mods.fresh_air;

import net.minecraft.entity.player.PlayerEntity;

public interface InteractingPlayer {
  PlayerInteractionManager getInteractionManager();

  PlayerEntity getSelf();
}
