package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public abstract class BaseCrateContainer extends Container {


    protected final IInventory inventory;
    private final int size;

    public BaseCrateContainer(int id, PlayerInventory playerInventory, IInventory inventory, int size, ContainerType<?extends BaseCrateContainer> containerType) {
        super(containerType, id);

        this.size = size;

        assertInventorySize(inventory, size);
        this.inventory = inventory;
        inventory.openInventory(playerInventory.player);

        drawUserInventory(playerInventory);

    }

    private void drawUserInventory(PlayerInventory playerInventory) {
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
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.inventory.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int slotIndex) {

        // Init empty itemstack
        ItemStack copyStack = ItemStack.EMPTY;

        // Get significant slot
        Slot slot = this.inventorySlots.get(slotIndex);

        // If slot has stuff
        if (slot != null && slot.getHasStack()) {

            // Get stuff from slot
            ItemStack stackFromSlot = slot.getStack();
            copyStack = stackFromSlot.copy();


            int playerInventorySize = this.inventorySlots.size() - size;
            if (slotIndex >= playerInventorySize) {

                // If slot is changed
                if (!this.mergeItemStack(stackFromSlot, 0, playerInventorySize, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stackFromSlot, playerInventorySize, this.inventorySlots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackFromSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
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
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.inventory.closeInventory(playerIn);
    }

}
