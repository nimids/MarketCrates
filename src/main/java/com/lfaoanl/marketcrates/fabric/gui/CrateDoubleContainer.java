package com.lfaoanl.marketcrates.fabric.gui;

import com.lfaoanl.marketcrates.fabric.core.CrateRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class CrateDoubleContainer extends BaseCrateContainer {

    private static final MenuType<CrateDoubleContainer> CONTAINER_REGISTRY = CrateRegistry.CRATE_DOUBLE_SCREEN;

    public CrateDoubleContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(12));

    }

    public CrateDoubleContainer(int id, Inventory playerInventory, Container inventory) {
        super(id, playerInventory, inventory, 12, CONTAINER_REGISTRY, true);
    }
}