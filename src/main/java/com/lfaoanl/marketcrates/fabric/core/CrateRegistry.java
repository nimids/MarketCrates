package com.lfaoanl.marketcrates.fabric.core;

import com.lfaoanl.marketcrates.common.Ref;
import com.lfaoanl.marketcrates.common.blocks.AbstractCrateBlock;
import com.lfaoanl.marketcrates.common.items.CrateItem;
import com.lfaoanl.marketcrates.fabric.blocks.CrateBlock;
import com.lfaoanl.marketcrates.fabric.blocks.CrateBlockEntity;
import com.lfaoanl.marketcrates.fabric.gui.CrateContainer;
import com.lfaoanl.marketcrates.fabric.gui.CrateDoubleContainer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;

public class CrateRegistry {
    public static final ResourceLocation CRATE_CHANNEL = new ResourceLocation(Ref.MODID, "crate_channel");

    public static String[] woodTypes = new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "crimson", "warped"};

    public static final HashMap<String, Item> items = new HashMap<>();
    public static final HashMap<String, Block> blocks = new HashMap<>();

    public static BlockEntityType<CrateBlockEntity> CRATE_BLOCK_ENTITY;

    public static MenuType<CrateDoubleContainer> CRATE_DOUBLE_SCREEN;
    public static MenuType<CrateContainer> CRATE_SCREEN;

    public static void preInit() {
        for (String type : woodTypes) {
            AbstractCrateBlock block = new CrateBlock();
            blocks.put(type, block);
            items.put(type, new CrateItem(block, new Item.Properties()));
        }
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> items.values().forEach(content::accept));

        CRATE_DOUBLE_SCREEN = Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Ref.MODID, "crate_double"), new MenuType<>(CrateDoubleContainer::new));
        CRATE_SCREEN = Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Ref.MODID, "crate"), new MenuType<>(CrateContainer::new));

    }

    public static void init() {

        for (String type : woodTypes) {
            ResourceLocation resourceLocation = new ResourceLocation(Ref.MODID, type + "_crate");
            Registry.register(BuiltInRegistries.BLOCK, resourceLocation, blocks.get(type));
            Registry.register(BuiltInRegistries.ITEM, resourceLocation, items.get(type));
        }

        ResourceLocation rlCrateBlockEntity = new ResourceLocation(Ref.MODID, "crate");

        Block[] blocksArray = blocks.values().toArray(new Block[0]);

        BlockEntityType<CrateBlockEntity> beBuilder = FabricBlockEntityTypeBuilder.create(CrateBlockEntity::new, blocksArray).build(null);
        CRATE_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, rlCrateBlockEntity, beBuilder);
    }

}
