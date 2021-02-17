package com.lfaoanl.marketcrates.tileentities;

import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import com.lfaoanl.marketcrates.core.ItemOrientation;
import com.lfaoanl.marketcrates.gui.CrateContainer;
import com.lfaoanl.marketcrates.gui.CrateDoubleContainer;
import com.lfaoanl.marketcrates.network.CratesPacketHandler;
import com.lfaoanl.marketcrates.network.packets.CrateItemsPacket;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class CrateTileEntity extends LockableTileEntity implements IForgeTileEntity {

    //    private NonNullList<ItemStack> stacks = NonNullList.withSize(6, ItemStack.EMPTY);
    private NonNullList<ItemOrientation> stacks = NonNullList.withSize(12, ItemOrientation.EMPTY);

    private boolean isDouble = false;

    public CrateTileEntity() {
        super(CrateRegistry.CRATE_TILE.get());
    }

    public NonNullList<ItemOrientation> getItems() {
        return stacks;
    }

    public boolean isDoubleCrate() {
        if (getBlockState().isAir()) {
            return this.isDouble;
        }
        // Store items to be able to retreive it after the block is broken
        this.isDouble = getBlockState().get(CrateBlock.TYPE).isDouble();

        return this.isDouble;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.getItems().set(index, new ItemOrientation(stack));
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemOrientation::isEmpty);
    }

    public ItemStack getStackInSlot(int index) {
        // TODO increase size of `stacks` and check if crateIsDouble to allow the supplied index
        return this.getItems().get(index).getItemStack();
    }

    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(ItemOrientation.toItemStack(stacks), index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        sendContents();
    }

    public ItemStack removeStackFromSlot(int index) {
        ItemOrientation orientation = index >= 0 && index < stacks.size() ? stacks.set(index, ItemOrientation.EMPTY) : ItemOrientation.EMPTY;

        return orientation.getItemStack();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.crate");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        if (isDoubleCrate()) {
            return new CrateDoubleContainer(id, player, this);
        }
        return new CrateContainer(id, player, this);
    }

    @Override
    public int getSizeInventory() {
//        if (isDoubleCrate()) {
        return 12;
//        }
//        return 6;
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    public void clear() {
        this.getItems().clear();
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        this.stacks = loadFromNbt(nbt);
    }

    private NonNullList<ItemOrientation> loadFromNbt(CompoundNBT nbt) {
        NonNullList<ItemStack> items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, items);
        return ItemOrientation.toItemOrientation(items);
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        ItemStackHelper.saveAllItems(compound, ItemOrientation.toItemStack(this.stacks));

        return compound;
    }

    public void receiveContents(NonNullList<ItemStack> stacks) {
        this.stacks = ItemOrientation.toItemOrientation(stacks);
    }

    public void sendContents() {
        if (!world.isRemote()) {
            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_CHUNK.with(() -> (Chunk) this.getWorld().getChunk(this.getPos()));
            CratesPacketHandler.INSTANCE.send(target, new CrateItemsPacket(this.getPos(), ItemOrientation.toItemStack(stacks)));
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = super.getUpdateTag();

        ItemStackHelper.saveAllItems(updateTag, ItemOrientation.toItemStack(stacks));

        return updateTag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);

        stacks = loadFromNbt(tag);
    }
}
