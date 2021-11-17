package com.lfaoanl.marketcrates.core;

import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonProxy {

    public Level getWorld() {
        return null;
    }


    public void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {}

}
