package com.lukekaalim.mods.rendering_hooks.v0;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class EntityRendererRegistry {
  Map<EntityType<?>, Set<EntityRenderer>> renderers = new HashMap<>();

  public void RunPreRender(Entity entity, EntityRenderContext context) {
    var entityType = entity.getType();
    var entityRenderers = renderers.get(entityType);
    if (entityRenderers == null)
      return;

    for (var renderer : entityRenderers) {
      renderer.Render(entity, context);
    }
  }

  public void Register(EntityType<?> type, EntityRenderer renderer) {
    var entityRenderers = renderers.getOrDefault(type, new HashSet<>());
    if (!renderers.containsKey(type))
      renderers.put(type, entityRenderers);
    
    entityRenderers.add(renderer);
  }
}
