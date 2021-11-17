package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.References;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrateScreen extends BaseCrateScreen<CrateContainer> {

    private ResourceLocation GUI = new ResourceLocation(References.MODID, "textures/gui/crate.png");

    public CrateScreen(CrateContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    ResourceLocation getGuiTexture() {
        return GUI;
    }
}