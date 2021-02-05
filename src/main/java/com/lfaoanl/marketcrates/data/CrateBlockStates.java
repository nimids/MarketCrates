package com.lfaoanl.marketcrates.data;

import com.lfaoanl.marketcrates.References;
import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.blocks.states.CrateType;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;

public class CrateBlockStates extends BlockStateProvider {


    public CrateBlockStates(DataGenerator dataGenerator, ExistingFileHelper helper) {
        super(dataGenerator, References.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        System.out.println("Start registering things");
        for (String material : CrateRegistry.woodTypes) {
            System.out.println(material);
            Block block = CrateRegistry.blocks.get(material).get();


            simpleBlockItem(block, getModel(block, material, CrateType.DEFAULT).model);
            VariantBlockStateBuilder builder = getVariantBuilder(block);

            for (CrateType crateType : CrateType.all()) {
                for (Map.Entry<Direction, Number> entry : getRotations().entrySet()) {
                    builder.partialState()
                            .with(CrateBlock.TYPE, crateType)
                            .with(CrateBlock.FACING, entry.getKey())
                            .modelForState()
                            .modelFile(getModel(block, material, crateType).model)
                            .rotationY((int) entry.getValue())
                            .addModel();

                }
            }
        }
    }

    private ConfiguredModel getModel(Block block, String woodType, CrateType crateType) {
        String texture = String.format("minecraft:block/%s_planks", woodType);

        return new ConfiguredModel(models().withExistingParent(woodType + "_" + crateType.getResource(), modLoc(crateType.getResource()))
                .texture("particle", texture)
                .texture("material", texture));
    }

    private HashMap<Direction, Number> getRotations() {
        HashMap<Direction, Number> rot = new HashMap<>();

        rot.put(Direction.NORTH, 0);
        rot.put(Direction.EAST, 90);
        rot.put(Direction.SOUTH, 180);
        rot.put(Direction.WEST, 270);

        return rot;
    }

}
