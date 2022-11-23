package com.lfaoanl.marketcrates.forge.core;

import com.lfaoanl.marketcrates.common.Ref;
import com.lfaoanl.marketcrates.common.gui.BaseCrateContainer;
import com.lfaoanl.marketcrates.common.gui.CrateScreen;
import com.lfaoanl.marketcrates.common.render.CrateBlockEntityRenderer;
import com.lfaoanl.marketcrates.forge.MarketCratesForge;
import com.lfaoanl.marketcrates.forge.blocks.CrateBlock;
import com.lfaoanl.marketcrates.forge.blocks.CrateBlockEntity;
import com.lfaoanl.marketcrates.forge.gui.CrateContainer;
import com.lfaoanl.marketcrates.forge.gui.CrateDoubleContainer;
import com.tterrag.registrate.util.entry.*;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class CrateRegistry {

//    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Ref.MODID);

    //    public static final RegistryObject<MenuType<CrateContainer>> CONTAINER_CRATE = registerContainer("crate", (IContainerFactory<CrateContainer>) (windowId, playerInventory, data) -> new CrateContainer(windowId, playerInventory));
    public static final MenuEntry<BaseCrateContainer> CONTAINER_CRATE = MarketCratesForge.REGISTRATE.object("crate")
            .menu(
                    (type, windowId, inv) -> new CrateContainer(windowId, inv, type),
                    () -> CrateScreen::new
            )
            .register();

    public static final MenuEntry<BaseCrateContainer> CONTAINER_CRATE_DOUBLE = MarketCratesForge.REGISTRATE.object("crate_double")
            .menu(
                    (type, windowId, inv) -> new CrateDoubleContainer(windowId, inv, type),
                    () -> CrateScreen::new
            )
            .register();
//    public static final RegistryObject<MenuType<CrateDoubleContainer>> CONTAINER_CRATE_DOUBLE = registerContainer("crate_double", (IContainerFactory<CrateDoubleContainer>) (windowId, playerInv, data) -> new CrateDoubleContainer(windowId, playerInv));


    private enum WoodType {

        OAK("oak", Items.OAK_SLAB,  Items.OAK_FENCE),
        SPRUCE("spruce", Items.SPRUCE_SLAB,  Items.SPRUCE_FENCE),
        BIRCH("birch", Items.BIRCH_SLAB,  Items.BIRCH_FENCE),
        JUNGLE("jungle", Items.JUNGLE_SLAB,  Items.JUNGLE_FENCE),
        ACACIA("acacia", Items.ACACIA_SLAB,  Items.ACACIA_FENCE),
        DARK_OAK("dark_oak", Items.DARK_OAK_SLAB,  Items.DARK_OAK_FENCE),
        CRIMSON("crimson", Items.CRIMSON_SLAB,  Items.CRIMSON_FENCE),
        WARPED("warped", Items.WARPED_SLAB,  Items.WARPED_FENCE);

        private final String name;
        private final Item fence;
        private final Item slab;
        WoodType(String name, Item craftingSlab, Item craftingFence) {
            this.name = name;
            this.fence = craftingFence;
            this.slab = craftingSlab;
        }
    }

    public static class RegisteredWood {
        final BlockEntry<CrateBlock> block;
        final ItemEntry<BlockItem> item;
        public final BlockEntityEntry<CrateBlockEntity> blockentity;

        RegisteredWood(BlockEntry<CrateBlock> block, ItemEntry<BlockItem> item, BlockEntityEntry<CrateBlockEntity> blockentity) {
            this.block = block;
            this.item = item;
            this.blockentity = blockentity;
        }
    }

    public static final HashMap<String, RegisteredWood> registered = new HashMap<>();

    public static void init() {

        for (WoodType type : WoodType.values()) {

            String name = type.name + "_crate";
            AtomicReference<WoodType> atomicWoodType= new AtomicReference<>(type);
            BlockEntry<CrateBlock> block = MarketCratesForge.REGISTRATE.object(name)
//                    .block(CrateBlock::new).register();;//BLOCKS.register(type + "_crate", CrateBlock::new);
                    .block(CrateBlock::new)
                    .properties(p -> p.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                            prov.models().withExistingParent(ctx.getName(), new ResourceLocation("block/" + type + "_crate"))))
                    .recipe((ctx, prov) -> {
                        ShapedRecipeBuilder.shaped(ctx.getEntry())
                                .pattern("FFF").pattern("SSS")
                                .define('F', atomicWoodType.get().fence)
                                .define('S', atomicWoodType.get().slab)
                                .unlockedBy("has_wood_slab", prov.has(Items.SPRUCE_SLAB))
                                .save(prov);
                    })
                    .item()
                        .color(() -> () -> (stack, index) -> 0xFFFF0000)
                        .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("item/" + type + "_crate")))
                        .build()
                    .blockEntity(CrateBlockEntity::new)
                        .renderer(() -> CrateBlockEntityRenderer::new)
                        .build()
                    .register();

            ItemEntry<BlockItem> item = (ItemEntry<BlockItem>) block.<Item, BlockItem>getSibling(Registry.ITEM_REGISTRY);
            BlockEntityEntry<CrateBlockEntity> blockentity = BlockEntityEntry.cast(block.getSibling(ForgeRegistries.BLOCK_ENTITIES));


            registered.put(type.name, new RegisteredWood(block, item, blockentity));
        }

    }

//
//    private static Block[] validCrates() {
//        Block[] b = new Block[woodTypes.length];
//
//        for (int i = 0; i < woodTypes.length; i++) {
//            String type = woodTypes[i];
//            b[i] = blocks.get(type).get();
//        }
//
//        return b;
//    }

//    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerContainer(String key, MenuType.MenuSupplier<T> supplier)
//    {
//        MenuType<T> type = new MenuType<>(supplier);
//        return CONTAINERS.register(key, () -> type);
//    }

}
