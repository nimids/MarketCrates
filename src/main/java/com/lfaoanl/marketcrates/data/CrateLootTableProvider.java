package com.lfaoanl.marketcrates.data;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lfaoanl.marketcrates.References;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class CrateLootTableProvider implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    protected final Map<ResourceLocation, LootTable> lootTables = new HashMap<>();

    private final DataGenerator generator;

    protected CrateLootTableProvider(DataGenerator generator) {
        this.generator = generator;
    }


    protected void createLootTables() {
        for (String material : CrateRegistry.woodTypes) {
            Block block = CrateRegistry.blocks.get(material).get();

            registerSelfDrop(block);
        }
    }


    @Override
    public void act(DirectoryCache cache) {
        lootTables.clear();
        Path outFolder = generator.getOutputFolder();

        createLootTables();

        ValidationTracker validator = new ValidationTracker(LootParameterSets.GENERIC, function -> null, lootTables::get);
        lootTables.forEach((name, table) -> LootTableManager.validateLootTable(validator, name, table));
        Multimap<String, String> problems = validator.getProblems();
        if (!problems.isEmpty()) {
            problems.forEach((name, table) -> System.out.println("Found validation problem in " + name + ": " + table));
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            lootTables.forEach((name, table) -> {
                Path out = getPath(outFolder, name);

                try {
                    IDataProvider.save(GSON, cache, LootTableManager.toJson(table), out);
                } catch (IOException e) {
                    System.out.println("Couldn't save loot table " + out);
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            });
        }
    }

    @Override
    public String getName() {
        return "Loot Tables: " + References.MODID;
    }

    protected LootPool.Builder createLootPoolBuilder() {
        return LootPool.builder().acceptCondition(SurvivesExplosion.builder());
    }

    protected void register(Block block, LootPool.Builder... pools) {
        LootTable.Builder builder = LootTable.builder();
        for (LootPool.Builder pool : pools) {
            builder.addLootPool(pool);
        }
        register(block, builder);
    }

    protected void registerSelfDrop(Block block) {
        register(block, singleItem(block));
    }

    private Path getPath(Path outFolder, ResourceLocation name) {
        return outFolder.resolve("data/" + name.getNamespace() + "/loot_tables/" + name.getPath() + ".json");
    }

    private void register(Block block, LootTable.Builder table) {
        register(block.getRegistryName(), table);
    }

    private void register(ResourceLocation registryName, LootTable.Builder table) {
        if (lootTables.put(toTableLoc(registryName), table.setParameterSet(LootParameterSets.BLOCK).build()) != null) {
            throw new IllegalStateException("Duplicate loot table: " + table);
        }
    }

    private LootPool.Builder singleItem(IItemProvider in) {
        return createLootPoolBuilder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(in));
    }

    private ResourceLocation toTableLoc(ResourceLocation registryName) {
        return new ResourceLocation(registryName.getNamespace(), "blocks/" + registryName.getPath());
    }
}