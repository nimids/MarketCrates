package com.lfaoanl.marketcrates.blocks;

import com.lfaoanl.marketcrates.common.ItemOrientation;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import com.lfaoanl.marketcrates.gui.CrateContainer;
import com.lfaoanl.marketcrates.gui.CrateDoubleContainer;
import com.lfaoanl.marketcrates.network.CratesPacketHandler;
import com.lfaoanl.marketcrates.network.packets.CrateItemsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class CrateBlockEntity extends AbstractCrateBlockEntity {

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(CrateRegistry.CRATE_TILE.get(), pos, state);
    }

    @Override
    public void sendContents() {
        if (!level.isClientSide()) {
            // FORGE send packet to client
            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_CHUNK.with(() -> (LevelChunk) this.getLevel().getChunk(this.getBlockPos()));
            CratesPacketHandler.INSTANCE.send(target, new CrateItemsPacket(this.getBlockPos(), ItemOrientation.toItemStack(stacks)));
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        //TODO Make CrateDoubleContainer to include isDoubleCrate as parameter maybe the abstract classes not needed anymore
        if (isDoubleCrate()) {
            return new CrateDoubleContainer(id, player, this);
        }
        return new CrateContainer(id, player, this);
    }
}
