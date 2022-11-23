package com.lfaoanl.marketcrates.forge.blocks;

import com.lfaoanl.marketcrates.common.blocks.AbstractCrateBlock;
import com.lfaoanl.marketcrates.forge.core.CrateRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class CrateBlock extends AbstractCrateBlock {

    public CrateBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        String descriptionId = this.getDescriptionId();
        String crateTypeName = descriptionId.substring(descriptionId.lastIndexOf('.') + 1);
        crateTypeName        = crateTypeName.substring(0, crateTypeName.indexOf("_crate"));

        CrateRegistry.RegisteredWood crateType = CrateRegistry.registered.get(crateTypeName);
        return crateType.blockentity.create(pos, state);
    }

    @Override
    protected void openGui(BlockState state, Level world, Player player, BlockPos pos) {
        // FORGE open inventory
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof CrateBlockEntity) {
            NetworkHooks.openGui((ServerPlayer) player, (CrateBlockEntity) tile, tile.getBlockPos());
        }

    }
}
