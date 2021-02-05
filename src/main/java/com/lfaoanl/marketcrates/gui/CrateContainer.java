package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.core.CrateRegistry;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;


public class CrateContainer extends BaseCrateContainer {


    public CrateContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(6));
    }

    public CrateContainer(int id, PlayerInventory playerInventory, IInventory inventory) {
        super(id, playerInventory, inventory, 6, CrateRegistry.CONTAINER_CRATE.get());

        // Container inventory

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlot(new Slot(inventory, i + j * 3, 71 + j * 18, 17 + i * 18));
            }
        }

    }

}
