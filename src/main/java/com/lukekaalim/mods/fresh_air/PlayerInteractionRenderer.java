package com.lukekaalim.mods.fresh_air;

import mikera.vectorz.Vector3;
import mikera.vectorz.Vector4;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;

public class PlayerInteractionRenderer implements WorldRenderable {

  @Override
  public void PrepareRender(WorldRenderContext context) {
    return;
    /*
    var client = context.gameRenderer.getClient();
    if (client.cameraEntity instanceof InteractingPlayer player) {
      PlayerInteractionManager im = player.getInteractionManager();
      var box = im.CreateInteractionBox(context.tickDelta);
      var interactibleEntities = im.CalculateInteractableEntities(context.tickDelta);
      var interactibleBlockEntities = im.CalculateInteractableBlockEntities(context.tickDelta);
      var targetedBlocks = im.CalculateTargetedBlock(context.tickDelta);

      RenderLine(new Vec3d(0, 0, 0), new Vec3d(0, 500, 0), new Vector4(1, 1, 1, 1), context);

      RenderBoxLines(box, new Vector4(0, 0, 1, 1), context);
      RenderText(client.cameraEntity, context);
      
      for (var entity : interactibleEntities) {
        if (entity == interactibleEntities.get(0))
          RenderSelected(client.cameraEntity, entity.getBoundingBox().getCenter(), context);
        
        RenderBoxLines(entity.getBoundingBox(), new Vector4(1, 0, 0, 1), context);
      }
      for (var blockEntity : interactibleBlockEntities) {
        if (interactibleEntities.size() == 0 && blockEntity == interactibleBlockEntities.get(0))
          RenderSelected(client.cameraEntity, VectorUtils.FromBlockPos(blockEntity.getPos()), context);
        
        RenderBoxLines(new Box(blockEntity.getPos()), new Vector4(1, 0, 1, 1), context);
      }
      for (var blockPos : targetedBlocks) {
        if (interactibleEntities.size() == 0 && interactibleBlockEntities.size() == 0 && blockPos == targetedBlocks.get(0))
          RenderSelected(client.cameraEntity, VectorUtils.FromBlockPos(blockPos), context);
        
        var color = blockPos == targetedBlocks.get(0) ? new Vector4(0, 1, 1, 1) : new Vector4(0, 0.5, 0.5, 1);
        RenderBoxLines(new Box(blockPos).expand(0.1), color, context);
      }
    
    }
    */
  }

  void RenderSelected(Entity player, Vec3d target, WorldRenderContext context) {    
    var start = player.getBoundingBox().getCenter();
    RenderLine(start, target, new Vector4(1, 1, 1, 1), context);
  }

  public void RenderText(Entity player, WorldRenderContext context) {
    var textRenderer = MinecraftClient.getInstance().textRenderer;
    var text = Text.of("hello!");
    var playerPos = player.getLerpedPos(context.tickDelta);
  
    context.matrices.push();
    context.matrices.translate((float)playerPos.x, (float)playerPos.y + 2f, (float)playerPos.z);
    context.matrices.multiply(context.camera.getRotation());
    context.matrices.scale(-0.025f, -0.025f, 0.025f);
    var matrix4f = context.matrices.peek()
      .getPositionMatrix();
    var redColor = ColorHelper.Argb.getArgb(255, 255, 100, 100);
    var blackColor = ColorHelper.Argb.getArgb(255, 0, 0, 0);
    textRenderer.draw(text, 0, 0, redColor, true, matrix4f, context.vertexProvider, false, blackColor, 255);
    context.matrices.pop();
  }

  public void RenderBoxLines(Box box, Vector4 color, WorldRenderContext context) {
    var centerPos = box.getCenter();
    box = box.offset(box.getCenter().negate());
    var buffer = context.vertexProvider.getBuffer(RenderLayer.getLines());

    context.matrices.push();
    context.matrices.translate(centerPos.x, centerPos.y, centerPos.z);
    
    WorldRenderer.drawBox(context.matrices, buffer, box, (float)color.x, (float)color.y, (float)color.z, (float)color.t);
    context.matrices.pop();
  }

  public void RenderLine(Vec3d start, Vec3d end, Vector4 color, WorldRenderContext context) {
    var buffer = context.outlineVertexProvider.getBuffer(RenderLayer.getLines());
  
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
  }
}
