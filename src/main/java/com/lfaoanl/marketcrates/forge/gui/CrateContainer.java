package com.lfaoanl.marketcrates.forge.gui;

import com.lfaoanl.marketcrates.common.gui.BaseCrateContainer;
import com.lfaoanl.marketcrates.forge.core.CrateRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class CrateContainer extends BaseCrateContainer {


//    private static final MenuType<CrateContainer> CONTAINER_REGISTRY = CrateRegistry.CONTAINER_CRATE.get();
//
    public CrateContainer(int id, Inventory playerInventory, MenuType<? extends BaseCrateContainer> type) {
        this(id, playerInventory, new SimpleContainer(6), type);

    }
//    public BaseCrateContainer(int id, Inventory playerInventory, Container inventory, int size, MenuType<? extends BaseCrateContainer> containerType, boolean isDouble) {
    public CrateContainer(int id, Inventory playerInventory, Container inventory, MenuType<? extends BaseCrateContainer> type) {
        super(id, playerInventory, inventory, 6, type, false);
    }

}
