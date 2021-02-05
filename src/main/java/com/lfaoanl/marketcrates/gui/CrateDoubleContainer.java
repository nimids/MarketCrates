package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

public class CrateDoubleContainer extends BaseCrateContainer {

    public CrateDoubleContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(12));

    }

    public CrateDoubleContainer(int id, PlayerInventory playerInventory, IInventory inventory) {
        super(id, playerInventory, inventory, 12, CrateRegistry.CONTAINER_CRATE_DOUBLE.get());

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
    }
}