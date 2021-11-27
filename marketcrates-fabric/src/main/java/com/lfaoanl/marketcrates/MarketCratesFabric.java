package com.lfaoanl.marketcrates;

import com.lfaoanl.marketcrates.common.MarketCrates;
import com.lfaoanl.marketcrates.common.CommonProxy;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.fabricmc.api.ModInitializer;

public class MarketCratesFabric implements ModInitializer {

    public MarketCratesFabric() {
        MarketCrates.proxy = new CommonProxy();

        CrateRegistry.preInit();

    }

    @Override
    public void onInitialize() {
        CrateRegistry.init();

    }
}
