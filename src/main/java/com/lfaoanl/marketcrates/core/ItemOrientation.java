package com.lfaoanl.marketcrates.core;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Quaternion;

import java.util.Random;
import java.util.function.Consumer;

public class ItemOrientation {

    private final ItemStack itemStack;

    private final float height;
    private final Quaternion[] rotation = new Quaternion[3];


    public static final ItemOrientation EMPTY = new ItemOrientation(ItemStack.EMPTY);

    private static final Quaternion HORIZONTAL = new Quaternion(85, 0, 0, true);

    @SuppressWarnings("unchecked")
    private static final Consumer<MatrixStack>[] cratePositions = new Consumer[]{
            (Object m) -> ((MatrixStack) m).translate(0.6, 0.1, 0.65), // Upper Left
            (Object m) -> ((MatrixStack) m).translate(0.6, 0.1, 0.4), // Middle left

            (Object m) -> ((MatrixStack) m).translate(0.6, 0.1, 0.12), // Lower left
            (Object m) -> ((MatrixStack) m).translate(0.4, 0.1, 0.65), // Upper right

            (Object m) -> ((MatrixStack) m).translate(0.4, 0.1, 0.4), // Middle right
            (Object m) -> ((MatrixStack) m).translate(0.4, 0.1, 0.12) // Lower right
    };

    public ItemOrientation(ItemStack itemStack) {
        this.itemStack = itemStack;
        height = 0.1f;

        int lowIncline = -10;
        int incline = 25;
        rotation[0] = new Quaternion(randomInt(lowIncline, incline), randomInt(45), randomInt(incline), true);
        rotation[1] = new Quaternion(randomInt(lowIncline, incline), randomInt(45), randomInt(incline), true);
        rotation[2] = new Quaternion(randomInt(lowIncline, incline), randomInt(45), randomInt(incline), true);
    }

    public static NonNullList<ItemOrientation> toItemOrientation(NonNullList<ItemStack> stacks) {
        NonNullList<ItemOrientation> list = NonNullList.withSize(stacks.size(), ItemOrientation.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            list.set(i, new ItemOrientation(stacks.get(i)));
        }
        return list;
    }

    public static NonNullList<ItemStack> toItemStack(NonNullList<ItemOrientation> stacks) {
        NonNullList<ItemStack> list = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            list.set(i, stacks.get(i).itemStack);
        }
        return list;
    }

    private float randomInt(int i) {
        return randomInt(-i, i);
    }

    private float randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public void render(int index, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay) {
        matrix.push();
        cratePositions[index].accept(matrix);

        int round = Math.max(1, Math.round(itemStack.getCount() / 21f));
        for (int i = 0; i < round; i++) {
            renderLayer(i, matrix, buffer, light, overlay);
        }

        matrix.pop();
    }

    private void renderLayer(int layer, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay) {
        matrix.push();

        matrix.translate(0, height * layer, 0);
        matrix.rotate(rotation[layer]);

        renderItem(matrix, buffer, light, overlay);

        matrix.pop();
    }

    private void renderItem(MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay) {
        matrix.rotate(HORIZONTAL);
        matrix.scale(0.75f, 0.75f, 0.75f);
        Minecraft.getInstance().getItemRenderer().renderItem(
                itemStack,
                ItemCameraTransforms.TransformType.GROUND,
                light,
                overlay,
                matrix,
                buffer);
    }

    public boolean isEmpty() {
        if (this.itemStack == ItemStack.EMPTY) {
            return true;
        } else if (this.itemStack.getItem() != Items.AIR) {
            return this.itemStack.getCount() <= 0;
        } else {
            return true;
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}


