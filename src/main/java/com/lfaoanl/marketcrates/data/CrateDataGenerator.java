package com.lfaoanl.marketcrates.data;

import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class CrateDataGenerator {

    public static void init(GatherDataEvent event) {


        DataGenerator generator = event.getGenerator();

        if(event.includeServer()) {
            // Recipes
            generator.addProvider(new CrateRecipeProvider(generator));
            // LootTable
            generator.addProvider(new CrateLootTableProvider(generator));
        }
        if(event.includeClient()) {
            // BlockStates
            System.out.println("LFAOANL: Data generator");
            generator.addProvider(new CrateBlockStates(generator, event.getExistingFileHelper()));

            // Items
        }
    }
}
