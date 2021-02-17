package com.lfaoanl.marketcrates.render;

import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.blocks.states.CrateType;
import com.lfaoanl.marketcrates.core.ItemOrientation;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class CrateTileEntityRenderer<T extends TileEntity> extends TileEntityRenderer<T> implements Function<TileEntityRendererDispatcher, CrateTileEntityRenderer> {

    private final Quaternion SOUTH = new Quaternion(0, 180, 0, true);
    private final Quaternion EAST = new Quaternion(0, 270, 0, true);
    private final Quaternion WEST = new Quaternion(0, 90, 0, true);
    private final Quaternion INCLINED = new Quaternion(-22.5f, 0, 0, true);


    public CrateTileEntityRenderer() {
        this(TileEntityRendererDispatcher.instance);
    }

    public CrateTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T tileEntity, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay) {
        matrix.push();

        BlockState blockState = tileEntity.getBlockState();

        applyDirection(matrix, blockState);

        if (blockState.get(CrateBlock.TYPE) == CrateType.INCLINED) {
            matrix.rotate(INCLINED);
            matrix.translate(0, 0, 0.12);
        } else {
            matrix.translate(0, 0.1f, 0);
        }

        CrateTileEntity crateTE = (CrateTileEntity) tileEntity;
        NonNullList<ItemOrientation> items = crateTE.getItems();
        for (int i = 0; i < 6; i++) {

            items.get(i).render(i, matrix, buffer, light, overlay);
        }

        if (crateTE.isDoubleCrate()) {
            matrix.push();

            matrix.translate(0, 0.5, 0);

            for (int i = 0; i < 6; i++) {

                items.get(i + 6).render(i, matrix, buffer, light, overlay);
            }

            matrix.pop();
        }

        matrix.pop();
    }

    private void applyDirection(MatrixStack matrix, BlockState blockState) {
        if (blockState.get(CrateBlock.FACING) == Direction.SOUTH) {
            matrix.translate(1, 0, 1);
            matrix.rotate(SOUTH);
        } else if (blockState.get(CrateBlock.FACING) == Direction.EAST) {
            matrix.rotate(EAST);
            matrix.translate(0, 0, -1);
        } else if (blockState.get(CrateBlock.FACING) == Direction.WEST) {
            matrix.rotate(WEST);
            matrix.translate(-1, 0, 0);
        }
    }

    @Override
    public CrateTileEntityRenderer apply(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        return new CrateTileEntityRenderer(tileEntityRendererDispatcher);
    }
}
