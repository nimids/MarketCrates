package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class CrateDoubleContainer extends BaseCrateContainer {

    public CrateDoubleContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(12));

    }

    public CrateDoubleContainer(int id, Inventory playerInventory, Container inventory) {
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