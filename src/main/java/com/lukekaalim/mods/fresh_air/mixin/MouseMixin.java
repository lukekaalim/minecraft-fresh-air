package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lukekaalim.mods.fresh_air.CameraOrbiter;
import com.lukekaalim.mods.fresh_air.FreshAirMod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;

@Mixin(Mouse.class)
public class MouseMixin {
	@Shadow
	@Final
	private MinecraftClient client;

  @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
  public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
    var clientWindowHandle = client.getWindow().getHandle();
    var isMouseScrollInMinecraftWindow = window == clientWindowHandle;
    var player = client.player;
    var inOverlayMenu = client.getOverlay() != null;
    var inScreen = client.currentScreen != null;
    if (isMouseScrollInMinecraftWindow && player != null && !inOverlayMenu && !inScreen) {
      var scaledVerticalScroll = (client.options.getDiscreteMouseScroll().getValue() != false ? Math.signum(vertical) : vertical) * this.client.options.getMouseWheelSensitivity().getValue();
      var camera = client.gameRenderer.getCamera();
      var orbiter = (CameraOrbiter)camera;
      orbiter.pushCameraDistance((float)scaledVerticalScroll);
      ci.cancel();
    }
  }

  @Redirect(
    method = "updateMouse",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
    )
  )
  public void onChangeLookDirection(ClientPlayerEntity changedEntity, double x, double y) {
    var camera = client.gameRenderer.getCamera();
    if (camera instanceof CameraOrbiter orbiter) {
      orbiter.rotateCamera((float)x * 0.15f, (float)y * 0.15f);
    }
  }
}
