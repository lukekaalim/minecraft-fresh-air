package com.lukekaalim.mods.fresh_air;

import com.lukekaalim.mods.interactions.EntityTarget;
import com.lukekaalim.mods.rendering_hooks.v0.EntityRenderContext;
import com.lukekaalim.mods.rendering_hooks.v0.EntityRenderer;

import mikera.vectorz.Vector4;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class PlayerEntityInteractionRenderer implements EntityRenderer {

  @Override
  public void Render(Entity entity, EntityRenderContext context) {
    var white = Vector4.of(1, 1, 1, 1);
    var pos = entity.getLerpedPos(context.tickDelta).add(0, 1.75f, 0);
    var cameraPos = context.camera.getPos();
    
    if (entity instanceof InteractingPlayer player) {
      PlayerInteractionManager manager = player.getInteractionManager();

      var target = manager.GetDefaultTarget();
      if (target == null)
        return;
        
      context.matrices.push();
      context.matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
      var bounds = target.Bounds();
  
      var lineBuffer = context.vertexConsumers.getBuffer(RenderLayer.LINES);
      DebugRenderUtils.WriteLineToBuffer(
        bounds.getCenter(),
        pos,
        white,
        lineBuffer,
        context.matrices
      );
      DebugRenderUtils.RenderBoxLines(bounds, white, context.vertexConsumers, context.matrices);
      
      DebugRenderUtils.RenderText(
        bounds.getCenter().add(0, bounds.maxY - bounds.minY / 2, 0),
        context.camera.getRotation(),
        Text.of("Selected"),
        context.vertexConsumers,
        context.matrices
      );
      context.matrices.pop();
    }
  }
}
