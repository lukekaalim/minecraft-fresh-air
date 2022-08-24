package com.lukekaalim.mods.fresh_air;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.ibm.icu.impl.Pair;
import com.lukekaalim.mods.interactions.BlockTarget;
import com.lukekaalim.mods.interactions.EntityTarget;
import com.lukekaalim.mods.interactions.Intention;
import com.lukekaalim.mods.interactions.Interaction;
import com.lukekaalim.mods.interactions.Target;
import com.lukekaalim.mods.interactions.registry.GlobalInteractionRegistry;

import mikera.vectorz.Vector3;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.World;

public class PlayerInteractionManager {
  public PlayerEntity player;
  public World world;

  public List<EntityTarget> entityTargets = new ArrayList<>();
  public List<BlockTarget> blockTargets = new ArrayList<>();

  public Optional<Target> lockedTarget = Optional.empty();

  public boolean locked;

  class TargetLock {

  }
  class EntityTargetLock extends TargetLock {

  }
  public enum BLOCK_TARGET_POSITION {
    GROUND,
    GROUND_IN_FRONT,
    LEG_HEIGHT_IN_FRONT,
    CHEST_HEIGHT_IN_FRONT,
    ABOVE_HEAD_IN_FRONT,
    ABOVE_HEAD,
  }
  class BlockTargetLock extends TargetLock {
    public static BLOCK_TARGET_POSITION[] BlockTargetPriority = new BLOCK_TARGET_POSITION[] {
      BLOCK_TARGET_POSITION.GROUND,
      BLOCK_TARGET_POSITION.GROUND_IN_FRONT,
      BLOCK_TARGET_POSITION.LEG_HEIGHT_IN_FRONT,
      BLOCK_TARGET_POSITION.CHEST_HEIGHT_IN_FRONT,
      BLOCK_TARGET_POSITION.ABOVE_HEAD_IN_FRONT,
      BLOCK_TARGET_POSITION.ABOVE_HEAD,
    };
  }

  class TargetCandidateSet {
    public List<TargetCandidate<EntityTarget>> entityCandidates;
    public Map<BLOCK_TARGET_POSITION, TargetCandidate<BlockTarget>> blockCandidates;
  }

  class TargetCandidate<T extends Target> {
    public T target;
    public Optional<Interaction> attack;
    public Optional<Interaction> use;

    public boolean GetIsValidCandidate() {
      return attack.isPresent() || use.isPresent();
    }

    public int GetPriority() {
      if (attack.isPresent() && use.isPresent())
        return Math.min(attack.get().priority, use.get().priority);
      if (attack.isPresent())
        return attack.get().priority;
      if (use.isPresent())
        return use.get().priority;
      return Integer.MIN_VALUE;
    }

    public TargetCandidate(T target, PlayerEntity player) {
      this.target = target;
      this.attack = GlobalInteractionRegistry.GetInteraction(player, target, Intention.ATTACK);
      this.use = GlobalInteractionRegistry.GetInteraction(player, target, Intention.USE);
    }
  }

  /*
   * Retrieve a broad list of all possible targets.
   * That is:
   *  - Entities within the EntityInteractionArea
   *  - Blocks within the BlockInteractionArea
   */
  Stream<TargetCandidate> GetAllTargetCandidates() {
    var entityInteractionArea = CalculateEntityInteractionArea();
    var entities = CalculateTargetableEntities(entityInteractionArea);
  }


  public Target GetDefaultTarget() {
    if (lockedTarget.isPresent()) {
      return lockedTarget.get();
    }
    if (entityTargets.size() > 0) {
      return entityTargets.get(0);
    }
    if (blockTargets.size() > 0) {
      var useableTargets = blockTargets
        .stream()
        .map(t -> t.blockState.isAir() ? Optional.<Interaction>empty() : GlobalInteractionRegistry.GetInteraction(player, t, Intention.USE))
        .filter(io -> io.isPresent())
        .map(io -> io.get())
        .sorted((a, b) -> b.priority - a.priority)
        .findFirst();
      if (useableTargets.isPresent())
        return useableTargets.get().target;
      return blockTargets.get(0);
    }
    return null;
  }

  public Optional<Interaction> GetSelectedUseInteraction() {
    var target = GetDefaultTarget();
    if (target == null)
      return Optional.empty();
    return GlobalInteractionRegistry.GetInteraction(player, target, Intention.USE);
  }
  public Optional<Interaction> GetSelectedAttackInteraction() {
    var target = GetDefaultTarget();
    if (target == null)
      return Optional.empty();
    return GlobalInteractionRegistry.GetInteraction(player, target, Intention.ATTACK);
  }

  public void Tick() {
    var entityBox = CreateInteractionBox();
    var nextEntityTargets = CalculateTargetableEntities(entityBox);
    var nextBlockTargets = CalculateBlockTargets();
    if (lockedTarget.isPresent()) {
      var prevLockedTarget = lockedTarget.get();
      if (prevLockedTarget instanceof EntityTarget prevLockedEntityTarget) {
        var nextLockedEntityTarget = nextEntityTargets
          .stream()
          .filter(et -> et.entity == prevLockedEntityTarget.entity)
          .findFirst();
        if (nextLockedEntityTarget.isPresent()) {
          lockedTarget = Optional.of(nextLockedEntityTarget.get());
        } else {
          lockedTarget = Optional.empty();
        }
      } else if (prevLockedTarget instanceof BlockTarget prevLockedBlockTarget) {
        var nextLockedBlockTarget = nextBlockTargets
          .stream()
          .filter(bt -> bt.blockPos.getY() == prevLockedBlockTarget.blockPos.getY())
          .findFirst();
        if (nextLockedBlockTarget.isPresent()) {
          lockedTarget = Optional.of(nextLockedBlockTarget.get());
        } else {
          lockedTarget = Optional.empty();
        }
      }
    }
    entityTargets = nextEntityTargets;
    blockTargets = nextBlockTargets;
  }

  public PlayerInteractionManager(PlayerEntity player) {
    this.player = player;
    this.world = player.world;
  }

  public Box CalculateEntityInteractionArea() {
    var playerBlockPosition = player.getPos();
    var playerYawDegrees = player.getYaw();
    var playerYawRadians = ((playerYawDegrees+90)/180 * Math.PI);
    var interactionSize = 2;

    var playerFacingDirection = new Vector3(Math.cos(playerYawRadians), interactionSize/2, Math.sin(playerYawRadians));
    var playerCenterVector = new Vector3(playerBlockPosition.x, playerBlockPosition.y, playerBlockPosition.z);

    var interactionOrigin = playerCenterVector.copy();

    interactionOrigin.add(playerFacingDirection.multiplyCopy(interactionSize));
    interactionOrigin.add(-0.5, -0.5, -0.5);

    var boxCenter = VectorUtils.FromVectorz(interactionOrigin);

    return Box.from(boxCenter).expand(interactionSize);
  }
  
  public Stream<EntityTarget> CalculateTargetableEntities(Box box) {


    var interactableEntities = player.world
      .getOtherEntities(player, box, e -> true)
      .stream()
      .sorted((a, b) -> (int)(a.squaredDistanceTo(player) - b.squaredDistanceTo(player)))
      .map(entity -> new EntityTarget(entity))
      .filter(target -> {
        var playerHeadPos = player.getPos().add(new Vec3d(0, 2, 0));
        var context = new BlockStateRaycastContext(
          playerHeadPos,
          target.entity.getBoundingBox().getCenter(),
          state -> !state.isAir()
        );
        var hit = world.raycast(context);
        return hit.getType() == BlockHitResult.Type.MISS;
      });

    return interactableEntities;
  }
  public List<Interaction> CalculateInteractableBlockEntities(float tickDelta) {
    var box = CreateInteractionBox();
    var blockEntities = BlockPos.stream(box)
      .map(blockPos -> GlobalInteractionRegistry.GetBlockUseInteractions(player, blockPos))
      .filter(option -> option.isPresent())
      .map(option -> option.get())
      .toList();

    return blockEntities;
  }

  public BlockPos GetDefaultBlockFocus() {
    var playerPos = player.getBlockPos();
    var playerYawDegrees = player.getYaw();
    var playerYawRadians = ((playerYawDegrees + 90)/180 * Math.PI);

    var playerFacingDirection = new Vector3(Math.cos(playerYawRadians), 1, Math.sin(playerYawRadians));
    var playerCenterVector = new Vector3(playerPos.getX(), playerPos.getY(), playerPos.getZ());

    var interactionOrigin = playerCenterVector.addCopy(playerFacingDirection);

    return new BlockPos(Math.round(interactionOrigin.x), Math.round(interactionOrigin.y), Math.round(interactionOrigin.z));
  }

  public List<BlockTarget> CalculateBlockTargets() {
    var startBlockPos = GetDefaultBlockFocus();
    var upBlockPos = startBlockPos.offset(Direction.UP);
    var downBlockPos = startBlockPos.offset(Direction.DOWN);
    var floorBlockPos = downBlockPos.offset(Direction.DOWN);
    
    return Arrays.asList(
      new BlockTarget(world, upBlockPos),
      new BlockTarget(world, startBlockPos),
      new BlockTarget(world, downBlockPos),
      new BlockTarget(world, floorBlockPos)
    ).stream().filter(target ->
      GlobalInteractionRegistry.GetBlockAttackInteractions(player, target.blockPos).isPresent() ||
      GlobalInteractionRegistry.GetBlockUseInteractions(player, target.blockPos).isPresent()
    ).toList();
  }

  void LockTarget() {

  }

  public void ToggleLock() {
    if (lockedTarget.isPresent()) {
      lockedTarget = Optional.empty();
    }
    lockedTarget = Optional.of(GetDefaultTarget());
  }
}
