package com.lfaoanl.marketcrates.blocks.states;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.shapes.VoxelShape;

import java.util.Arrays;
import java.util.Collection;

public enum CrateType implements IStringSerializable {

    DEFAULT("default", 8d, false),
    INCLINED("inclined", 14d, true),
    DOUBLE("double", 16d, true);

    private final String name;
    private final double height;
    private final boolean resoueceName;

    /*
    shapes.put("default", Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 8.0D, 16.0D));
    shapes.put("inclined", Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 14.0D, 16.0D));
    shapes.put("default_horizontal", Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 16.0D, 8.0D, 14.0D));
    shapes.put("inclined_horizontal", Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D));
     */
    CrateType(String name, double height, boolean resourceName) {
        this.name = name;
        this.height = height;
        this.resoueceName = resourceName;
    }

    public static Collection<CrateType> allValues() {
        return Arrays.asList(DEFAULT, INCLINED, DOUBLE);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public VoxelShape getShape(boolean horizontal) {
        if (horizontal) {
            return Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 16.0D, height, 14.0D);
        }
        return Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, height, 16.0D);
    }

    public boolean isDouble() {
        return this == DOUBLE;
    }

    public CrateType opposite() {
        return this == DEFAULT ? INCLINED : DEFAULT;
    }

    public String getResource() {

        return "crate" + (resoueceName ? "_" + this.name : "");
    }

    public static CrateType[] all() {
        return new CrateType[]{DEFAULT, DOUBLE, INCLINED};
    }

}
