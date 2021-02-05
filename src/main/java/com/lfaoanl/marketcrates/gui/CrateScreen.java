package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.References;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CrateScreen extends BaseCrateScreen<CrateContainer> {

    private ResourceLocation GUI = new ResourceLocation(References.MODID, "textures/gui/crate.png");

    public CrateScreen(CrateContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    ResourceLocation getGuiTexture() {
        return GUI;
    }
}