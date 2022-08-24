package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import com.lukekaalim.mods.fresh_air.CameraOrbiter;

import eu.midnightdust.midnightcontrols.client.MidnightInput;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

@Mixin(MidnightInput.class)
public class MidnightInputMixin {

  @Shadow(remap = false)
  double targetYaw;

  @Shadow(remap = false)
  double targetPitch;

  @Redirect(
    method = "onRender",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/network/ClientPlayerEntity;setYaw(F)V"
    )
  )
  void onSetYaw(ClientPlayerEntity player, float yaw) {
    var instance = MinecraftClient.getInstance();
    var camera = instance.gameRenderer.getCamera();
    var tickDelta = instance.getTickDelta();

    if (camera instanceof CameraOrbiter orbiter)
      orbiter.setCameraYaw((float)this.targetYaw * 0.1F * tickDelta);
  }

  @Redirect(
    method = "onRender",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/network/ClientPlayerEntity;setPitch(F)V"
    )
  )
  void onSetPitch(ClientPlayerEntity player, float pitch) {
    var instance = MinecraftClient.getInstance();
    var camera = instance.gameRenderer.getCamera();
    var tickDelta = instance.getTickDelta();

    if (camera instanceof CameraOrbiter orbiter)
      orbiter.setCameraPitch((float)this.targetPitch * 0.1F * tickDelta);
  }
}
