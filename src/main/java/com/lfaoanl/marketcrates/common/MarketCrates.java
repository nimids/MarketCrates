package com.lfaoanl.marketcrates.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;

public class MarketCrates {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger(Ref.MODID);

    public static IMarketCratesProxy proxy;

    public static Quaternionf QuaternionHelper(float x, float y, float z, boolean degrees) {
        if (degrees) {
            x *= (float)Math.PI / 180;
            y *= (float)Math.PI / 180;
            z *= (float)Math.PI / 180;
        }
        float f = (float) Math.sin(0.5f * x);
        float g = (float) Math.cos(0.5f * x);
        float h = (float) Math.sin(0.5f * y);
        float i = (float) Math.cos(0.5f * y);
        float j = (float) Math.sin(0.5f * z);
        float k = (float) Math.cos(0.5f * z);
        x = f * i * k + g * h * j;
        y = g * h * k - f * i * j;
        z = f * h * k + g * i * j;
        float w = g * i * k - f * h * j;
        return new Quaternionf(x, y, z, w);
    }
}
