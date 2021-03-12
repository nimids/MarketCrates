package com.lfaoanl.marketcrates.items;

import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.blocks.states.CrateType;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrateItem extends BlockItem {

    public CrateItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();

        // If item is same as BlockItem of targetted CrateBlock
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof CrateBlock && ((CrateBlock) block).getBlockItem().getItem().equals(this)) {

            // If the TileEntity is not already a double crate
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof CrateTileEntity && !((CrateTileEntity) tileEntity).isDoubleCrate()) {
                BlockState newState = tileEntity.getBlockState().with(CrateBlock.TYPE, CrateType.DOUBLE);
                PlayerEntity player = context.getPlayer();

                world.setBlockState(pos, newState, 2);
                ((CrateBlock) tileEntity.getBlockState().getBlock()).playSound(world, pos, player, SoundEvents.BLOCK_WOOD_PLACE);

                if (!player.isCreative()) {
                    context.getItem().setCount(context.getItem().getCount() - 1);
                }

                // SUCCES when client && COSNUME when server
                if (world.isRemote())  {
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.CONSUME;
            }
        }

        return super.onItemUse(context);
    }
}
