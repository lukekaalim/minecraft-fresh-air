package com.lukekaalim.mods.rendering_hooks.v0;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class EntityRenderContext {
  public MatrixStack matrices;
  public VertexConsumerProvider vertexConsumers;
  public Camera camera;
  public float tickDelta;
  public int light;
}
