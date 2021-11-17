package com.lfaoanl.marketcrates.items;

import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.blocks.states.CrateType;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrateItem extends BlockItem {

    public CrateItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();

        // If item is same as BlockItem of targetted CrateBlock
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof CrateBlock && ((CrateBlock) block).getBlockItem().getItem().equals(this)) {

            // If the TileEntity is not already a double crate
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof CrateTileEntity && !((CrateTileEntity) tileEntity).isDoubleCrate()) {
                BlockState newState = tileEntity.getBlockState().setValue(CrateBlock.TYPE, CrateType.DOUBLE);
                Player player = context.getPlayer();

                world.setBlock(pos, newState, 2);
                ((CrateBlock) tileEntity.getBlockState().getBlock()).playSound(world, pos, player, SoundEvents.WOOD_PLACE);

                if (!player.isCreative()) {
                    context.getItemInHand().setCount(context.getItemInHand().getCount() - 1);
                }

                // SUCCES when client && COSNUME when server
                return InteractionResult.sidedSuccess(world.isClientSide());
            }
        }

        return super.useOn(context);
    }
}
