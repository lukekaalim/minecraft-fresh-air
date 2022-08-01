package net.fabricmc.example;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public interface PlayerEntityRenderCallback {
     
  Event<PlayerEntityRenderCallback> EVENT = EventFactory.createArrayBacked(PlayerEntityRenderCallback.class,
      (listeners) -> (renderer, player, yaw, tickDelta, matricies, vertexConsumerProvider, lightLevel) -> {
          for (PlayerEntityRenderCallback listener : listeners) {
              var result = listener.renderPlayer(renderer, player, yaw, tickDelta, matricies, vertexConsumerProvider, lightLevel);

              if(result.getResult() != ActionResult.PASS) {
                return result;
              }
          }

      return TypedActionResult.pass(null);
  });

  TypedActionResult<RenderLayer> renderPlayer(
    PlayerEntityRenderer renderer,
    AbstractClientPlayerEntity player,
    float yaw,
    float tickDelta,
    MatrixStack matrices,
    VertexConsumerProvider vertexConsumerProvider,
    int lightLevel
  );
}