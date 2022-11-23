package com.lfaoanl.marketcrates.forge.gui;

import com.lfaoanl.marketcrates.common.gui.BaseCrateContainer;
import com.lfaoanl.marketcrates.forge.core.CrateRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class CrateDoubleContainer extends BaseCrateContainer {

    public CrateDoubleContainer(int id, Inventory playerInventory, MenuType<? extends BaseCrateContainer> type) {
        this(id, playerInventory, new SimpleContainer(12), type);

    }

    public CrateDoubleContainer(int id, Inventory playerInventory, Container inventory, MenuType<? extends BaseCrateContainer> type) {
        super(id, playerInventory, inventory, 12, type, true);
    }
}