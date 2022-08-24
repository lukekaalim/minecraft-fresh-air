package com.lukekaalim.mods.interactions.registry;

import com.lukekaalim.mods.interactions.Interaction;

public interface InteractionProvider {
  public Interaction GetInteraction(InteractionContext context);
}
