package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FireBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Random;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.datafixers.util.Pair;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final ScreenHandlerType<BoxScreenHandler> BOX_SCREEN_HANDLER = new ScreenHandlerType<BoxScreenHandler>(BoxScreenHandler::new);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		var item = new CustomItem(new FabricItemSettings().group(ItemGroup.MISC));
		var block = new CustomBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
		var blockItem = new BlockItem(block, new FabricItemSettings().group(ItemGroup.MISC));
		var blockEntityType = FabricBlockEntityTypeBuilder
			.create(CustomBlockEntity::new, block)
			.build(null);
		CustomBlockEntity.TYPE = blockEntityType;

		Registry.register(Registry.ITEM, new Identifier("tutorial", "custom_item"), item);
		Registry.register(Registry.BLOCK, new Identifier("tutorial", "custom_block"), block);
		Registry.register(Registry.ITEM, new Identifier("tutorial", "custom_block"), blockItem);

		Registry.register(Registry.BLOCK_ENTITY_TYPE, "tutorial:custom_block_entity", blockEntityType);

		var fireBlock = FlammableBlockRegistry.getDefaultInstance();
		//fireBlock.add(block, 1, 1);
	}
}

class CustomItem extends Item {
     
	public CustomItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		player.playSound(SoundEvents.ENTITY_POLAR_BEAR_WARNING, 1.0F, 1.0F);
		var stack = player.getStackInHand(hand);
		if (!world.isClient)
			stack.decrement(1);
		if (world.isClient && stack.getCount() % 2 == 0)
			player.sendMessage(Text.of("ODD!"), false);
		return TypedActionResult.pass(stack);
	}
}

class CustomBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
	public static BlockEntityType<CustomBlockEntity> TYPE;

	public Item flowerOfDesire;

	public CustomBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	} 

	@Override
	public Text getDisplayName() {
		return Text.of("Hello!");
	}

	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		var i = new ImplementedInventory() {
			@Override
			public DefaultedList<ItemStack> getItems() {
				return DefaultedList.of();
			}
		};
		return new BoxScreenHandler(syncId, playerInventory, i, flowerOfDesire);
	}

	@Override
	public void writeNbt(NbtCompound tag) {
			// Save the current value of the number to the tag
			var id = Registry.ITEM.getId(flowerOfDesire);
			tag.putString("flowerOfDesire:id", id.toString());

			super.writeNbt(tag);
	}
	@Override
	public void readNbt(NbtCompound tag) {
			super.readNbt(tag);
	 
			var id = new Identifier(tag.getString("flowerOfDesire:id"));
			flowerOfDesire = Registry.ITEM.get(id);
	}

	public void yearn() {
		var flowersIdentifier = new Identifier("minecraft", "flowers");

		var flowerItems = Registry.ITEM.streamTagsAndEntries()
			.filter(pair -> pair.getFirst().id().equals(flowersIdentifier))
			.map(pair -> pair.getSecond())
			.findFirst();

		if (!flowerItems.isPresent())
			return;

		var world = getWorld();
		if (world == null)
			return;
		var randomFlower = flowerItems.get().getRandom(world.random);
		if (randomFlower.isPresent()) {
			flowerOfDesire = randomFlower.get().value();
			markDirty();
		}
	}

	public boolean trySeduce(ItemStack offering) {
		var item = offering.getItem();
		if (!item.equals(flowerOfDesire) || offering.getCount() < 1)
			return false;

		offering.decrement(1);

		var offsetPos = pos.offset(Direction.UP);
		var state = world.getBlockState(offsetPos);
		if (state.isAir())
			world.setBlockState(offsetPos, FireBlock.getState(world, offsetPos));

		return true;
	}

	public void queryDesire(PlayerEntity player) {
		if (flowerOfDesire == null)
			return;
		
		player.openHandledScreen(this);

		if (!world.isClient)
			player.sendMessage(Text.of("I YEARN FOR A " + flowerOfDesire.getName().getString()), false);
	}
}

class CustomBlock extends BlockWithEntity {
	public CustomBlock(Settings settings) {
		super(Settings.of(Material.WOOD, MapColor.OAK_TAN).strength(10, 100));
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CustomBlockEntity(pos, state);
	}
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		var entity = world.getBlockEntity(pos);
		if (entity instanceof CustomBlockEntity blockEntity) {
			blockEntity.yearn();
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
			var stack = player.getStackInHand(hand);
			var entity = world.getBlockEntity(pos);

			if (entity instanceof CustomBlockEntity blockEntity) {
				if (stack.isEmpty()) {
					blockEntity.queryDesire(player);
				} else {
					blockEntity.trySeduce(stack);
				}
			}

			return ActionResult.SUCCESS;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
			return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
			return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
}