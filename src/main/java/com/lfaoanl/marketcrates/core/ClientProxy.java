package com.lfaoanl.marketcrates.core;

import com.lfaoanl.marketcrates.gui.CrateDoubleScreen;
import com.lfaoanl.marketcrates.gui.CrateScreen;
import com.lfaoanl.marketcrates.render.CrateTileEntityRenderer;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void init(final FMLClientSetupEvent event) {

        MenuScreens.register(CrateRegistry.CONTAINER_CRATE.get(), CrateScreen::new);
        MenuScreens.register(CrateRegistry.CONTAINER_CRATE_DOUBLE.get(), CrateDoubleScreen::new);

//        ClientRegistry.bindTileEntityRenderer(CrateRegistry.CRATE_TILE.get(), new CrateTileEntityRenderer());
    }

    public void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CrateRegistry.CRATE_TILE.get(), CrateTileEntityRenderer::new);
    }

    public Level getWorld() {
        return Minecraft.getInstance().level;
    }
}
