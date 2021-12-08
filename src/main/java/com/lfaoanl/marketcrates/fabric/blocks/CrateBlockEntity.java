package com.lfaoanl.marketcrates.fabric.blocks;

import com.lfaoanl.marketcrates.common.ItemOrientation;
import com.lfaoanl.marketcrates.common.blocks.AbstractCrateBlockEntity;
import com.lfaoanl.marketcrates.fabric.core.CrateRegistry;
import com.lfaoanl.marketcrates.fabric.gui.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class CrateBlockEntity extends AbstractCrateBlockEntity {

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(CrateRegistry.CRATE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void sendContents() {
        if (!level.isClientSide()) {
            // FORGE send packet to client
//            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_CHUNK.with(() -> (LevelChunk) this.getLevel().getChunk(this.getBlockPos()));
//            CratesPacketHandler.INSTANCE.send(target, new CrateItemsPacket(this.getBlockPos(), ItemOrientation.toItemStack(stacks)));

            FriendlyByteBuf data = PacketByteBufs.create();
            BlockPos blockPos = this.getBlockPos();
            data.writeBlockPos(blockPos);
            data.writeInt(stacks.size());

            for (ItemStack item : ItemOrientation.toItemStack(stacks)) {
                data.writeItem(item);
            }

            // Iterate over all players tracking a position in the world and send the packet to each player
            for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) this.getLevel(), blockPos)) {
                ServerPlayNetworking.send(player, CrateRegistry.CRATE_CHANNEL, data);

            }
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        if (isDoubleCrate()) {
            return new CrateDoubleContainer(id, player, this);
        }
        return new CrateContainer(id, player, this);
    }
}
