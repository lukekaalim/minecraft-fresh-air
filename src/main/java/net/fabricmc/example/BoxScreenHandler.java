package net.fabricmc.example;

import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BoxScreenHandler extends ScreenHandler {
  //private final Inventory inventory;
  public Item item;
  Identifier packet = new Identifier("example", "screen_packet");

  //This constructor gets called on the client when the server wants it to open the screenHandler,
  //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
  //sync this empty inventory with the inventory on the server.
  public BoxScreenHandler(int syncId, PlayerInventory playerInventory) {
    // CLIENT!
    this(syncId, playerInventory, new SimpleInventory(0), null);
  }

  //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
  //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
  public BoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Item item) {
    super(ExampleMod.BOX_SCREEN_HANDLER, syncId);
    // SERVER!
    if (!playerInventory.player.world.isClient()) {
      S2CPlayChannelEvents.REGISTER.register((ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels) -> {
        this.item = item;
      });
    }
    else {
      ClientPlayNetworking.registerGlobalReceiver(packet, (client, handler, buf, responseSender) -> {
        this.item = item;
      });
    }

    this.item = item;

    slots.clear();
  }

  @Override
  public boolean canUse(PlayerEntity player) {
    return true;
    //return this.inventory.canPlayerUse(player);
  }

  // Shift + Player Inv Slot
  @Override
  public ItemStack transferSlot(PlayerEntity player, int invSlot) {
    return ItemStack.EMPTY;
    /*
    ItemStack newStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(invSlot);
    if (slot != null && slot.hasStack()) {
        ItemStack originalStack = slot.getStack();
        newStack = originalStack.copy();
        if (invSlot < this.inventory.size()) {
            if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
            return ItemStack.EMPTY;
        }

        if (originalStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }
    }

    return newStack;
    */
  }
}

