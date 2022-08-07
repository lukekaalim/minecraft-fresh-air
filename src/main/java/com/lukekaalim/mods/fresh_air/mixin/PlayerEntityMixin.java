package com.lukekaalim.mods.fresh_air.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import com.lukekaalim.mods.fresh_air.FreshAirMod;
import com.lukekaalim.mods.fresh_air.InteractingPlayer;
import com.lukekaalim.mods.fresh_air.PlayerInteractionManager;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements InteractingPlayer {
  PlayerInteractionManager playerInteractionManager;

  @Inject(
    method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;FLcom/mojang/authlib/GameProfile;Lnet/minecraft/network/encryption/PlayerPublicKey;)V",
    at = @At("TAIL")
  )
  private void constructPlayerEntity(CallbackInfo ci) {
    FreshAirMod.LOGGER.info("PLAYER INIT");
    playerInteractionManager = new PlayerInteractionManager((PlayerEntity)(Object)this);
  }

  @Override
  public PlayerInteractionManager getInteractionManager() {
    return playerInteractionManager;
  }
}
