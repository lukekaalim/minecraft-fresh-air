package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lukekaalim.mods.fresh_air.InteractingPlayer;
import com.lukekaalim.mods.fresh_air.PlayerInteractionManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.HitResult;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

  @Shadow
  HitResult crosshairTarget;

  @Shadow
  ClientPlayerEntity player;
  
  @Inject(
    method = "doAttack",
    at = @At("HEAD"),
    cancellable = true
  )
  void doAttack(CallbackInfoReturnable<Boolean> ci) {
    if (player instanceof InteractingPlayer interactingPlayer) {
      PlayerInteractionManager interactionManager = interactingPlayer.getInteractionManager();
      var attackInteractionOption = interactionManager.GetSelectedAttackInteraction();
      if (attackInteractionOption.isPresent()) {
        var attackInteraction = attackInteractionOption.get();
        crosshairTarget = attackInteraction.target.Hit();
      } else {
        ci.setReturnValue(false);
        ci.cancel();
      }
    }
  }
}
