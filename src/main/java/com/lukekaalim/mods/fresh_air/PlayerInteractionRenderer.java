package com.lukekaalim.mods.fresh_air;

import mikera.vectorz.Vector3;
import mikera.vectorz.Vector4;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;

public class PlayerInteractionRenderer implements WorldRenderable {

  @Override
  public void PrepareRender(WorldRenderContext context) {
    var client = context.gameRenderer.getClient();
    if (client.cameraEntity instanceof InteractingPlayer player) {
      PlayerInteractionManager im = player.getInteractionManager();
      var box = im.CreateInteractionBox();
      var interactibleEntities = im.CalculateInteractableEntities();
      var interactibleBlockEntities = im.CalculateInteractableBlockEntities();
      var targetedBlocks = im.CalculateTargetedBlock();

      RenderLine(new Vec3d(0, 0, 0), new Vec3d(0, 500, 0), new Vector4(1, 1, 1, 1), context);

      RenderBoxLines(box, new Vector4(0, 0, 1, 1), context);
      for (var entity : interactibleEntities) {
        if (entity == interactibleEntities.get(0)) {
          var start = client.cameraEntity.getBoundingBox().getCenter();
          var end = entity.getBoundingBox().getCenter();
          RenderLine(start, end, new Vector4(1, 1, 1, 1), context);
        }
        RenderBoxLines(entity.getBoundingBox(), new Vector4(1, 0, 0, 1), context);
      }
      for (var blockEntity : interactibleBlockEntities) {
        if (interactibleEntities.size() == 0 && blockEntity == interactibleBlockEntities.get(0)) {
          var start = client.cameraEntity.getBoundingBox().getCenter();
          var end = VectorUtils.FromBlockPos(blockEntity.getPos());
          RenderLine(start, end, new Vector4(1, 1, 1, 1), context);
        }
        RenderBoxLines(new Box(blockEntity.getPos()), new Vector4(0, 0, 1, 1), context);
      }
      for (var blockPos : targetedBlocks) {
        if (interactibleEntities.size() == 0 && interactibleBlockEntities.size() == 0 && blockPos == targetedBlocks.get(0)) {
          var start = client.cameraEntity.getBoundingBox().getCenter();
          var end = VectorUtils.FromBlockPosCenter(blockPos);
          RenderLine(start, end, new Vector4(1, 1, 1, 1), context);
        }
        var color = blockPos == targetedBlocks.get(0) ? new Vector4(0, 1, 1, 1) : new Vector4(0, 0.5, 0.5, 1);
        RenderBoxLines(new Box(blockPos).expand(0.1), color, context);
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

  void RenderLine(Vec3d start, Vec3d end, Vector4 color, WorldRenderContext context) {
    var buffer = context.vertexProvider.getBuffer(RenderLayer.getLines());
    var cameraPos = context.camera.getPos();
  
    context.matrices.push();
    context.matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

    var entry = context.matrices.peek();
    var pMatrix = entry.getPositionMatrix();
    var nMatrix = entry.getNormalMatrix();

    buffer.vertex(pMatrix, (float)start.x, (float)start.y, (float)start.z)
      .color((float)color.x, (float)color.y, (float)color.z, (float)color.t)
      .normal(nMatrix, 1.0f, 0.0f, 0.0f)
      .next();
    buffer.vertex(pMatrix, (float)end.x, (float)end.y, (float)end.z)
      .color((float)color.x, (float)color.y, (float)color.z, (float)color.t)
      .normal(nMatrix, 1.0f, 0.0f, 0.0f)
      .next();

    context.matrices.pop();
  }
}
