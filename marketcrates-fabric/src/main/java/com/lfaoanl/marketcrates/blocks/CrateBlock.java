package com.lfaoanl.marketcrates.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrateBlock extends AbstractCrateBlock {

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    protected void openGui(BlockState state, Level world, Player player, BlockPos pos) {
        //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
        //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
        MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);

        if (screenHandlerFactory != null) {
            //With this call the server will request the client to open the appropriate Screenhandler
            player.openMenu(screenHandlerFactory); //openHandledScreen(screenHandlerFactory);
        }
    }
}
