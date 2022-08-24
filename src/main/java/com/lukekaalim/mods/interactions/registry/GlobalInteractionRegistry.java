package com.lukekaalim.mods.interactions.registry;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.lukekaalim.mods.interactions.BlockTarget;
import com.lukekaalim.mods.interactions.EntityTarget;
import com.lukekaalim.mods.interactions.Intention;
import com.lukekaalim.mods.interactions.Interaction;
import com.lukekaalim.mods.interactions.Target;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class GlobalInteractionRegistry {

  public static Set<InteractionProvider> useInteractionHandlers = new HashSet<>();
  public static void RegisterInteractionProvider(InteractionProvider provider) {
    useInteractionHandlers.add(provider);
  }
  
  public static Optional<Interaction> GetBlockUseInteractions(PlayerEntity player, BlockPos blockPos) {
    var target = new BlockTarget(player.world, blockPos);
    var intention = Intention.USE;

    return GetInteraction(player, target, intention);
  }
  public static Optional<Interaction> GetEntityUseInteractions(PlayerEntity player, Entity entity) {
    var target = new EntityTarget(entity);
    var intention = Intention.USE;

    return GetInteraction(player, target, intention);
  }
  public static Optional<Interaction> GetBlockAttackInteractions(PlayerEntity player, BlockPos blockPos) {
    var target = new BlockTarget(player.world, blockPos);
    var intention = Intention.ATTACK;

    return GetInteraction(player, target, intention);
  }
  public static Optional<Interaction> GetEntityAttackInteractions(PlayerEntity player, Entity entity) {
    var target = new EntityTarget(entity);
    var intention = Intention.ATTACK;

    return GetInteraction(player, target, intention);
  }

  public static Optional<Interaction> GetInteraction(PlayerEntity player, Target target, Intention intention) {
    var mainHandContext = new InteractionContext(player, player.getMainHandStack(), target, intention);
    var offHandContext = new InteractionContext(player, player.getOffHandStack(), target, intention);
    for (var handler : useInteractionHandlers) {
      if (intention == Intention.USE) {
        var offHandInteraction = handler.GetInteraction(offHandContext);
        if (offHandInteraction != null)
          return Optional.of(offHandInteraction);
      }
      var mainHandInteraction = handler.GetInteraction(mainHandContext);
      if (mainHandInteraction != null)
        return Optional.of(mainHandInteraction);
    }
    return Optional.empty();
  }
}
