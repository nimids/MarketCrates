package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.References;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CrateDoubleScreen extends BaseCrateScreen<CrateDoubleContainer> {

    private ResourceLocation GUI = new ResourceLocation(References.MODID, "textures/gui/crate_double.png");

    public CrateDoubleScreen(CrateDoubleContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    ResourceLocation getGuiTexture() {
        return GUI;
    }
}