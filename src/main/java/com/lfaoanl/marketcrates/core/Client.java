package com.lfaoanl.marketcrates.core;

import com.lfaoanl.marketcrates.gui.BaseCrateContainer;
import com.lfaoanl.marketcrates.gui.CrateDoubleContainer;
import com.lfaoanl.marketcrates.gui.CrateDoubleScreen;
import com.lfaoanl.marketcrates.gui.CrateScreen;
import com.lfaoanl.marketcrates.render.CrateTileEntityRenderer;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class Client {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void init(final FMLClientSetupEvent event) {

        ScreenManager.registerFactory(CrateRegistry.CONTAINER_CRATE.get(), CrateScreen::new);
        ScreenManager.registerFactory(CrateRegistry.CONTAINER_CRATE_DOUBLE.get(), CrateDoubleScreen::new);

        ClientRegistry.bindTileEntityRenderer(CrateRegistry.CRATE_TILE.get(), new CrateTileEntityRenderer());
    }
}
