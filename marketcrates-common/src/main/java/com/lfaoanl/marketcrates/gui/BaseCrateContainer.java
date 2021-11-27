package com.lfaoanl.marketcrates.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class BaseCrateContainer extends AbstractContainerMenu {


    protected final Container inventory;
    private final int size;

    public BaseCrateContainer(int id, Inventory playerInventory, Container inventory, int size, MenuType<?extends BaseCrateContainer> containerType, boolean isDouble) {
        super(containerType, id);

        if (isDouble) {
            // Left
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    this.addSlot(new Slot(inventory, i + j * 3, 35 + j * 18, 17 + i * 18));
                }
            }

            // Right
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    this.addSlot(new Slot(inventory, 6 + i + j * 3, 107 + j * 18, 17 + i * 18));
                }
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    this.addSlot(new Slot(inventory, i + j * 3, 71 + j * 18, 17 + i * 18));
                }
            }
        }

        this.size = size;

        checkContainerSize(inventory, size);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);

        drawUserInventory(playerInventory);

    }

    private void drawContainerSlots() {
        //TODO dynamically calculate x values with an offset
//        for (int i = 0; i < 3; ++i) {
//            for (int j = 0; j < 2; ++j) {
//                this.addSlot(new Slot(inventory, i + j * 3, 71 + j * 18, 17 + i * 18));
//            }
//        }
    }

    private void drawUserInventory(Inventory playerInventory) {
        // Player inventory
        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        // Hotbar
        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player playerIn) {
        return this.inventory.stillValid(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack quickMoveStack(Player playerIn, int slotIndex) {

        // Init empty itemstack
        ItemStack copyStack = ItemStack.EMPTY;

        // Get significant slot
        Slot slot = this.slots.get(slotIndex);

        // If slot has stuff
        if (slot != null && slot.hasItem()) {

            // Get stuff from slot
            ItemStack stackFromSlot = slot.getItem();
            copyStack = stackFromSlot.copy();


            int playerInventorySize = this.slots.size() - size;
            if (slotIndex >= playerInventorySize) {

                // If slot is changed
                if (!this.moveItemStackTo(stackFromSlot, 0, playerInventorySize, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackFromSlot, playerInventorySize, this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackFromSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackFromSlot.getCount() == copyStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stackFromSlot);
        }

        return copyStack;
    }

    /**
     * Called when the container is closed.
     */
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.inventory.stopOpen(playerIn);
    }

}
