package com.lfaoanl.marketcrates;

import com.lfaoanl.marketcrates.core.Client;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import com.lfaoanl.marketcrates.data.CrateDataGenerator;
import com.lfaoanl.marketcrates.network.CratesPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(References.MODID)
public class MarketCrates {

    // Directly reference a log4j logger.
//    public static final Logger LOGGER = LogManager.getLogger(References.MODID);
    public static MarketCrates INSTANCE;


    public MarketCrates() {

        INSTANCE = this;

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        CrateRegistry.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CratesPacketHandler::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CrateDataGenerator::init);
    }


}
