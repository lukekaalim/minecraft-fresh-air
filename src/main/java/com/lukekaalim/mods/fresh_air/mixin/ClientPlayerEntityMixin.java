package com.lukekaalim.mods.fresh_air.mixin;

import mikera.vectorz.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lukekaalim.mods.fresh_air.CameraOrbiter;

import net.minecraft.client.MinecraftClient;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

  @Inject(method = "tickNewAi", at = @At("TAIL"), cancellable = true)
  void onNewAiTick(CallbackInfo ci) {
    var player = (ClientPlayerEntity)(Object)this;
    var input = player.input;
    var client = MinecraftClient.getInstance();
    var camera = client.gameRenderer.getCamera();

    var x = input.movementSideways;
    var y = input.movementForward;
    var direction = new Vector2(x, y);

    var angleRadians = Math.atan2(y, x);
    var mag = direction.magnitude();

    if (camera instanceof CameraOrbiter orbiter) {
      var cameraAngleDegrees = orbiter.getCameraYaw();

      var angleDegrees = (float)(angleRadians * 180 / Math.PI) - 90 + cameraAngleDegrees;
      
      if (mag > 0) {
        player.setYaw(angleDegrees);
      }
      player.bodyYaw = MathHelper.lerp(0.001f, (float)player.bodyYaw, angleDegrees);
  
      player.sidewaysSpeed = 0;
      player.forwardSpeed = (float)mag;
    }
  }
}
