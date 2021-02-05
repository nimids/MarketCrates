package com.lfaoanl.marketcrates.blocks.states;

import net.minecraft.state.EnumProperty;

import java.util.Collection;

public class CrateTypeProperty extends EnumProperty<CrateType> {

    protected CrateTypeProperty(String name, Class<CrateType> valueClass, Collection<CrateType> allowedValues) {
        super(name, valueClass, allowedValues);
    }

    public static CrateTypeProperty create(String type) {
        return new CrateTypeProperty(type, CrateType.class, CrateType.allValues());
    }
}
