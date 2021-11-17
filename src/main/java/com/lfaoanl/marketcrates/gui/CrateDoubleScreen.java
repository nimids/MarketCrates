package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.References;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class CrateDoubleScreen extends BaseCrateScreen<CrateDoubleContainer> {

    private ResourceLocation GUI = new ResourceLocation(References.MODID, "textures/gui/crate_double.png");

    public CrateDoubleScreen(CrateDoubleContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    ResourceLocation getGuiTexture() {
        return GUI;
    }
}