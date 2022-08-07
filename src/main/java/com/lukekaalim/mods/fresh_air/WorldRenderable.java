package com.lukekaalim.mods.fresh_air;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * An interface for something that can manage rendering things.
*/
public interface WorldRenderable {
  void PrepareRender(WorldRenderContext context);

  public class WorldRenderContext {
    public VertexConsumerProvider vertexProvider;
    public Camera camera;
    public GameRenderer gameRenderer;
    public MatrixStack matrices;
    public float tickDelta;
  }

  public static HashMap<Identifier, WorldRenderable> RENDERABLES = new HashMap<Identifier, WorldRenderable>();
}
