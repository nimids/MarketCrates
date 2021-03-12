package com.lfaoanl.marketcrates;

import com.lfaoanl.marketcrates.core.ClientProxy;
import com.lfaoanl.marketcrates.core.CommonProxy;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import com.lfaoanl.marketcrates.network.CratesPacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(References.MODID)
public class MarketCrates {

    // Directly reference a log4j logger.
//    public static final Logger LOGGER = LogManager.getLogger(References.MODID);
    public static MarketCrates INSTANCE;
    private static final boolean DEBUG = false;

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public MarketCrates() {

        INSTANCE = this;

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        log("Init CrateRegistry");
        CrateRegistry.init();
        log("Done initialising CrateRegistry");

        log("ClientOnly registry");
        DistExecutor.runWhenOn(Dist.CLIENT, MarketCrates::clientInit);
        log("Done! - ClientOnly registry");

        log("Init CratePacketHandler");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CratesPacketHandler::init);
        log("Done! - Init CratePacketHandler");

    }

    private void log(String message) {
        if (!DEBUG) {
            return;
        }
        System.out.println(message);
    }

    private static Runnable clientInit() {
        return () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientProxy::init);
    }


}
