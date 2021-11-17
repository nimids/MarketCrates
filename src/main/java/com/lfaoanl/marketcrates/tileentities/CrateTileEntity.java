package com.lfaoanl.marketcrates.tileentities;

import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import com.lfaoanl.marketcrates.core.ItemOrientation;
import com.lfaoanl.marketcrates.gui.CrateContainer;
import com.lfaoanl.marketcrates.gui.CrateDoubleContainer;
import com.lfaoanl.marketcrates.network.CratesPacketHandler;
import com.lfaoanl.marketcrates.network.packets.CrateItemsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class CrateTileEntity extends BaseContainerBlockEntity implements IForgeBlockEntity {

    //    private NonNullList<ItemStack> stacks = NonNullList.withSize(6, ItemStack.EMPTY);
    private NonNullList<ItemOrientation> stacks = NonNullList.withSize(12, ItemOrientation.EMPTY);

    private boolean isDouble = false;

    public CrateTileEntity(BlockPos pos, BlockState state) {
        super(CrateRegistry.CRATE_TILE.get(), pos, state);
    }

    public NonNullList<ItemOrientation> getItems() {
        return stacks;
    }

    public boolean isDoubleCrate() {
        if (getBlockState().isAir()) {
            return this.isDouble;
        }
        // Store items to be able to retreive it after the block is broken
        this.isDouble = getBlockState().getValue(CrateBlock.TYPE).isDouble();

        return this.isDouble;
    }

    public void setItem(int index, ItemStack stack) {
        this.getItems().set(index, new ItemOrientation(stack));
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemOrientation::isEmpty);
    }

    public ItemStack getItem(int index) {
        // TODO increase size of `stacks` and check if crateIsDouble to allow the supplied index
        return this.getItems().get(index).getItemStack();
    }

    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ContainerHelper.removeItem(ItemOrientation.toItemStack(stacks), index, count);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        sendContents();
    }

    public ItemStack removeItemNoUpdate(int index) {
        ItemOrientation orientation = index >= 0 && index < stacks.size() ? stacks.set(index, ItemOrientation.EMPTY) : ItemOrientation.EMPTY;

        return orientation.getItemStack();
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.crate");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        if (isDoubleCrate()) {
            return new CrateDoubleContainer(id, player, this);
        }
        return new CrateContainer(id, player, this);
    }

    @Override
    public int getContainerSize() {
//        if (isDoubleCrate()) {
        return 12;
//        }
//        return 6;
    }

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    public void clearContent() {
        this.getItems().clear();
    }

    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.stacks = loadFromNbt(nbt);
    }

    private NonNullList<ItemOrientation> loadFromNbt(CompoundTag nbt) {
        NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, items);
        return ItemOrientation.toItemOrientation(items);
    }

    public CompoundTag save(CompoundTag compound) {
        super.save(compound);

        ContainerHelper.saveAllItems(compound, ItemOrientation.toItemStack(this.stacks));

        return compound;
    }

    public void receiveContents(NonNullList<ItemStack> stacks) {
        this.stacks = ItemOrientation.toItemOrientation(stacks);
    }

    public void sendContents() {
        if (!level.isClientSide()) {
            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_CHUNK.with(() -> (LevelChunk) this.getLevel().getChunk(this.getBlockPos()));
            CratesPacketHandler.INSTANCE.send(target, new CrateItemsPacket(this.getBlockPos(), ItemOrientation.toItemStack(stacks)));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();

        ContainerHelper.saveAllItems(updateTag, ItemOrientation.toItemStack(stacks));

        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);

        stacks = loadFromNbt(tag);
    }
}
