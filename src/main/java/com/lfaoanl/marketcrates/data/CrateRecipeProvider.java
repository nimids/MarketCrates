package com.lfaoanl.marketcrates.data;

import com.lfaoanl.marketcrates.References;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class CrateRecipeProvider extends RecipeProvider {

    private final Blocks facadeBlocks = new Blocks();

    public CrateRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        for (String material : CrateRegistry.woodTypes) {
            RegistryObject<Block> registry = CrateRegistry.blocks.get(material);
            Block block = registry.get();

                ShapedRecipeBuilder.shapedRecipe(block)
                        .patternLine("FFF")
                        .patternLine("SSS")
                        .key('F', getBlock(material, "fence"))
                        .key('S', getBlock(material, "slab"))
                        .setGroup(References.MODID)
                        .addCriterion("has_wood_slab", hasItem(getBlock(material, "slab")))
                        .build(consumer, resourceLocation(registry.getId()));

        }

    }


    protected ResourceLocation resourceLocation(ResourceLocation location) {
        return new ResourceLocation(location.getNamespace(), location.getPath());
    }

    private Block getBlock(String material, String name) {
        System.out.println(material + " - " + name);
        String finalName = String.format("%s_%s", material.toUpperCase(), name.toUpperCase());
        System.out.println(finalName);
        try {
            return (Block) Blocks.class.getDeclaredField(finalName).get(facadeBlocks);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
