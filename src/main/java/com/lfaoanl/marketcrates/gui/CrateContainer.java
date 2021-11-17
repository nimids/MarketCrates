package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;


public class CrateContainer extends BaseCrateContainer {


    public CrateContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(6));
    }

    public CrateContainer(int id, Inventory playerInventory, Container inventory) {
        super(id, playerInventory, inventory, 6, CrateRegistry.CONTAINER_CRATE.get());

        // Container inventory

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlot(new Slot(inventory, i + j * 3, 71 + j * 18, 17 + i * 18));
            }
        }

    }

}
