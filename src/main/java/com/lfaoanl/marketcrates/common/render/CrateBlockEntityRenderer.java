package com.lfaoanl.marketcrates.common.render;

import com.lfaoanl.marketcrates.common.blocks.AbstractCrateBlock;
import com.lfaoanl.marketcrates.common.blocks.AbstractCrateBlockEntity;
import com.lfaoanl.marketcrates.common.blocks.states.CrateType;
import com.lfaoanl.marketcrates.common.ItemOrientation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

public class CrateBlockEntityRenderer<H extends AbstractCrateBlockEntity> implements BlockEntityRenderer<H> {

    private final Quaternionf SOUTH = new Quaternionf(0, 1, 0, 0);
    private final Quaternionf EAST = new Quaternionf(0, 1/Math.sqrt(2), 0, -1/Math.sqrt(2));
    private final Quaternionf WEST = new Quaternionf(0, 1/Math.sqrt(2), 0, 1/Math.sqrt(2));
    private final double INCLINE_ANGLE = -22.5f * Math.PI / 180;
    private final Quaternionf INCLINED = new Quaternionf(Math.sin(INCLINE_ANGLE/2), 0, 0, Math.cos(INCLINE_ANGLE/2));

    public CrateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(H tileEntity, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int light, int overlay) {
        matrix.pushPose();

        BlockState blockState = tileEntity.getBlockState();

        applyDirection(matrix, blockState);

        if (blockState.getValue(AbstractCrateBlock.TYPE) == CrateType.INCLINED) {
            matrix.mulPose(INCLINED);
            matrix.translate(0, 0, 0.12);
        } else {
            matrix.translate(0, 0.1f, 0);
        }

        NonNullList<ItemOrientation> items = tileEntity.getItems();
        for (int i = 0; i < 6; i++) {

            items.get(i).render(i, matrix, buffer, light, overlay);
        }

        if (tileEntity.isDoubleCrate()) {
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
        if (blockState.getValue(AbstractCrateBlock.FACING) == Direction.SOUTH) {
            matrix.translate(1, 0, 1);
            matrix.mulPose(SOUTH);
        } else if (blockState.getValue(AbstractCrateBlock.FACING) == Direction.EAST) {
            matrix.mulPose(EAST);
            matrix.translate(0, 0, -1);
        } else if (blockState.getValue(AbstractCrateBlock.FACING) == Direction.WEST) {
            matrix.mulPose(WEST);
            matrix.translate(-1, 0, 0);
        }
    }

}
