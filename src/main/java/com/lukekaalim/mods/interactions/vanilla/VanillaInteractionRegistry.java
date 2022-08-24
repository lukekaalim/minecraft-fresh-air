package com.lukekaalim.mods.interactions.vanilla;

import com.lukekaalim.mods.interactions.BlockTarget;
import com.lukekaalim.mods.interactions.EntityTarget;
import com.lukekaalim.mods.interactions.Interaction;
import com.lukekaalim.mods.interactions.registry.GlobalInteractionRegistry;
import com.lukekaalim.mods.interactions.registry.InteractionContext;
import com.lukekaalim.mods.interactions.registry.InteractionProvider;

import net.minecraft.block.DoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryEntry.Direct;

public class VanillaInteractionRegistry implements InteractionProvider {

  public Interaction GetAttackInteraction(InteractionContext context) {
    if (context.target instanceof EntityTarget entityTarget) {
      if (entityTarget.entity instanceof LivingEntity livingEntity) {
        return new AttackInteraction(context.player, context.item, livingEntity);
      }
    } else if (context.target instanceof BlockTarget blockTarget) {
      if (blockTarget.block.getHardness() >= 0f && !blockTarget.blockState.isAir()) {
        return new MineInteraction(context.player, context.item, blockTarget);
      }
    }

    return null;
  }
  public Interaction GetItemUseInteraction(InteractionContext context) {
    return null;
  }

  public Interaction GetEntityUseInteraction(InteractionContext context, EntityTarget entityTarget) {
    if (entityTarget.entity instanceof LivingEntity livingEntity) {
      var interaction = new Interaction();
      interaction.target = entityTarget;
      return interaction;
    }
    return null;
  }
  public Interaction GetBlockUseInteraction(InteractionContext context, BlockTarget blockTarget) {
    if (blockTarget.block instanceof DoorBlock) {
      var interaction = new DoorInteraction();
      interaction.target = blockTarget;
      return interaction;
    }
    var item = context.item.getItem();
    if (item instanceof BlockItem blockItem) {
      var placeablePosition = Direction.stream().anyMatch(direction -> {
        var offset = blockTarget.blockPos.offset(direction, 1);
        return !blockTarget.world.getBlockState(offset).isAir();
      });

      if (placeablePosition && blockTarget.blockState.isAir()) {
        return new PlaceInteraction(context.item, blockTarget);
      }
    }
    return null;
  }

  public Interaction GetUseTargetInteraction(InteractionContext context) {
    if (context.target instanceof BlockTarget blockTarget) {
      return GetBlockUseInteraction(context, blockTarget);
    }
    if (context.target instanceof EntityTarget entityTarget) {
      return GetEntityUseInteraction(context, entityTarget);
    }
    return null;
  }

  @Override
  public Interaction GetInteraction(InteractionContext context) {
    switch (context.intention) {
      case ATTACK:
        return GetAttackInteraction(context);
      case USE:
        var useTargetInteraction = GetUseTargetInteraction(context);
        if (useTargetInteraction != null)
          return useTargetInteraction;
        return GetItemUseInteraction(context);
    }
    
    return null;
  }
}
