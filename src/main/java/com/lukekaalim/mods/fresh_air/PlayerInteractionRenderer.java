package com.lukekaalim.mods.fresh_air;

import mikera.vectorz.Vector4;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShapes;

public class PlayerInteractionRenderer implements WorldRenderable {

  @Override
  public void PrepareRender(WorldRenderContext context) {
    var client = context.gameRenderer.getClient();
    if (client.cameraEntity instanceof InteractingPlayer player) {
      PlayerInteractionManager im = player.getInteractionManager();
      var box = im.CreateInteractionBox();
      RenderBoxLines(box, new Vector4(0, 0, 1, 1), context);
      for (var entity : im.CalculateInteractableEntities()) {
        RenderBoxLines(entity.getBoundingBox(), new Vector4(1, 0, 0, 2), context);
      }
    }
  }

  void RenderBoxLines(Box box, Vector4 color, WorldRenderContext context) {
    var centerPos = box.getCenter();
    box = box.offset(box.getCenter().negate());
    var buffer = context.vertexProvider.getBuffer(RenderLayer.getLines());

    var cameraPos = context.camera.getPos();
    var offsetX = centerPos.x - cameraPos.x;
    var offsetY = centerPos.y - cameraPos.y;
    var offsetZ = centerPos.z - cameraPos.z;

    context.matrices.push();
    context.matrices.translate(offsetX, offsetY, offsetZ);
    WorldRenderer.drawBox(context.matrices, buffer, box, (float)color.x, (float)color.y, (float)color.z, (float)color.t);
    context.matrices.pop();
  }
}
