package com.lfaoanl.marketcrates.render;

import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.blocks.states.CrateType;
import com.lfaoanl.marketcrates.core.ItemOrientation;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrateTileEntityRenderer implements BlockEntityRenderer<CrateTileEntity> {

    private final Quaternion SOUTH = new Quaternion(0, 180, 0, true);
    private final Quaternion EAST = new Quaternion(0, 270, 0, true);
    private final Quaternion WEST = new Quaternion(0, 90, 0, true);
    private final Quaternion INCLINED = new Quaternion(-22.5f, 0, 0, true);

    public CrateTileEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(CrateTileEntity tileEntity, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int light, int overlay) {
        matrix.pushPose();

        BlockState blockState = tileEntity.getBlockState();

        applyDirection(matrix, blockState);

        if (blockState.getValue(CrateBlock.TYPE) == CrateType.INCLINED) {
            matrix.mulPose(INCLINED);
            matrix.translate(0, 0, 0.12);
        } else {
            matrix.translate(0, 0.1f, 0);
        }

        CrateTileEntity crateTE = tileEntity;
        NonNullList<ItemOrientation> items = crateTE.getItems();
        for (int i = 0; i < 6; i++) {

            items.get(i).render(i, matrix, buffer, light, overlay);
        }

        if (crateTE.isDoubleCrate()) {
            matrix.pushPose();

            matrix.translate(0, 0.5, 0);

            for (int i = 0; i < 6; i++) {

                items.get(i + 6).render(i, matrix, buffer, light, overlay);
            }

            matrix.popPose();
        }

        matrix.popPose();
    }

    private void applyDirection(PoseStack matrix, BlockState blockState) {
        if (blockState.getValue(CrateBlock.FACING) == Direction.SOUTH) {
            matrix.translate(1, 0, 1);
            matrix.mulPose(SOUTH);
        } else if (blockState.getValue(CrateBlock.FACING) == Direction.EAST) {
            matrix.mulPose(EAST);
            matrix.translate(0, 0, -1);
        } else if (blockState.getValue(CrateBlock.FACING) == Direction.WEST) {
            matrix.mulPose(WEST);
            matrix.translate(-1, 0, 0);
        }
    }

}
