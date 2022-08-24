package com.lukekaalim.mods.fresh_air;

import com.lukekaalim.mods.fresh_air.WorldRenderable.WorldRenderContext;

import mikera.vectorz.Vector4;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class DebugRenderUtils {
  
  public static void WriteLineToBuffer(Vec3d start, Vec3d end, Vector4 color, VertexConsumer buffer, MatrixStack matrices) {  
    var entry = matrices.peek();
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


  public static void RenderText(Vec3d position, Quaternion rotation, Text text, VertexConsumerProvider provider, MatrixStack matrices) {
    var textRenderer = MinecraftClient.getInstance().textRenderer;
  
    matrices.push();
    matrices.translate((float)position.x, (float)position.y + 2f, (float)position.z);
    matrices.multiply(rotation);
    matrices.scale(-0.025f, -0.025f, 0.025f);

    var matrix4f = matrices.peek()
      .getPositionMatrix();
    var redColor = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    var blackColor = ColorHelper.Argb.getArgb(100, 0, 0, 0);

    textRenderer.draw(text, 0, 0, redColor, true, matrix4f, provider, false, blackColor, 255);

    matrices.pop();
  }

  public static void RenderBoxLines(Box box, Vector4 color, VertexConsumerProvider vertexProvider, MatrixStack matrices) {
    var centerPos = box.getCenter();
    box = box.offset(box.getCenter().negate());
    var buffer = vertexProvider.getBuffer(RenderLayer.getLines());

    matrices.push();
    matrices.translate(centerPos.x, centerPos.y, centerPos.z);
    
    WorldRenderer.drawBox(matrices, buffer, box, (float)color.x, (float)color.y, (float)color.z, (float)color.t);
    matrices.pop();
  }
}
