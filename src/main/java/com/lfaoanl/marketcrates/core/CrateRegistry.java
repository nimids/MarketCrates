package com.lfaoanl.marketcrates.core;

import com.lfaoanl.marketcrates.References;
import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.gui.CrateContainer;
import com.lfaoanl.marketcrates.gui.CrateDoubleContainer;
import com.lfaoanl.marketcrates.items.CrateItem;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class CrateRegistry {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.MODID);


    public static final RegistryObject<MenuType<CrateContainer>> CONTAINER_CRATE = CONTAINERS.register("crate", () -> IForgeContainerType.create((windowId, inv, data) -> new CrateContainer(windowId, inv)));
    public static final RegistryObject<MenuType<CrateDoubleContainer>> CONTAINER_CRATE_DOUBLE = CONTAINERS.register("crate_double", () -> IForgeContainerType.create((windowId, inv, data) -> new CrateDoubleContainer(windowId, inv)));


    public static String[] woodTypes = new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "crimson", "warped"};

    public static final RegistryObject<BlockEntityType<CrateTileEntity>> CRATE_TILE = TILES.register("crate", () -> BlockEntityType.Builder.of(CrateTileEntity::new, CrateRegistry.validCrates()).build(null));

    public static final HashMap<String, RegistryObject<Item>> items = new HashMap<>();
    public static final HashMap<String, RegistryObject<Block>> blocks = new HashMap<>();


    public static void init() {

        for (String type : woodTypes) {
            System.out.println("Register: " + type);
            RegistryObject<Block> block = BLOCKS.register(type + "_crate", CrateBlock::new);
            RegistryObject<Item> item = ITEMS.register(type + "_crate", () -> new CrateItem(block.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

            blocks.put(type, block);
            items.put(type, item);
        }

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    private static Block[] validCrates() {
        Block[] b = new Block[woodTypes.length];

        for (int i = 0; i < woodTypes.length; i++) {
            String type = woodTypes[i];
            b[i] = blocks.get(type).get();
        }

        return b;
    }
}
