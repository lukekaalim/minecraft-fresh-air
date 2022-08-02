package net.fabricmc.fresh_air.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fresh_air.CameraOrbiter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.render.Camera;

@Mixin(Mouse.class)
public class MouseMixin {
	@Shadow
	@Final
	private MinecraftClient client;

  @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
  public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
    var clientWindowHandle = MinecraftClient.getInstance().getWindow().getHandle();
    var isMouseScrollInMinecraftWindow = window == clientWindowHandle;
    var player = this.client.player;
    var inOverlayMenu = this.client.getOverlay() != null;
    var inScreen = this.client.currentScreen != null;
    if (isMouseScrollInMinecraftWindow && player != null && !inOverlayMenu && !inScreen) {
      var scaledVerticalScroll = (this.client.options.getDiscreteMouseScroll().getValue() != false ? Math.signum(vertical) : vertical) * this.client.options.getMouseWheelSensitivity().getValue();
      var camera = this.client.gameRenderer.getCamera();
      var orbiter = (CameraOrbiter)camera;
      orbiter.pushCameraDistance((float)scaledVerticalScroll);
      ci.cancel();
    }
  }
}
