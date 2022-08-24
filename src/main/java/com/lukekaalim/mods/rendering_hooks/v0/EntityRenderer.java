package com.lukekaalim.mods.rendering_hooks.v0;

import net.minecraft.entity.Entity;

public interface EntityRenderer {
  void Render(Entity entity, EntityRenderContext context);
}
