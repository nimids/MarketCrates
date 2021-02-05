package com.lfaoanl.marketcrates.blocks;

import com.lfaoanl.marketcrates.blocks.states.CrateType;
import com.lfaoanl.marketcrates.blocks.states.CrateTypeProperty;
import com.lfaoanl.marketcrates.core.ItemOrientation;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class CrateBlock extends Block implements IWaterLoggable {

    private static final Properties properties = Properties.create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final CrateTypeProperty TYPE = CrateTypeProperty.create("type");

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Item inclinable = Items.STICK;

    private NonNullList<ItemOrientation> oldItems;

    public CrateBlock() {
        super(properties);

        this.setDefaultState(this.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(TYPE, CrateType.DEFAULT)
                .with(WATERLOGGED, false));


    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(TYPE);
        builder.add(WATERLOGGED);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }


    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }

    /**
     * Opacity 1 to fix the lighting on the block below
     *
     * @param state
     * @param worldIn
     * @param pos
     * @return
     */
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        boolean isWaterLogged = fluidstate.getFluid() == Fluids.WATER;
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATERLOGGED, isWaterLogged);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        boolean horizontal = false;

        if (state.get(FACING).getAxis() == Direction.Axis.X) {
            horizontal = true;
        }

        return state.get(TYPE).getShape(horizontal);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return super.getDrops(state, builder);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            System.out.println("onReplaced");
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof CrateTileEntity) {
                if (state.get(TYPE).isDouble()) {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), getBlockItem());
                }
                InventoryHelper.dropInventoryItems(world, pos, (CrateTileEntity) tileentity);
//                worldIn.updateComparatorOutputLevel(pos, this); TODO

                this.oldItems = ((CrateTileEntity) world.getTileEntity(pos)).getItems();
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    public ItemStack getBlockItem() {
        return new ItemStack(Item.getItemFromBlock(this));
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if (state.get(TYPE).isDouble()) {
            for (int i = 0; i < 12; i++) {
                InventoryHelper.spawnItemStack((World) world, pos.getX(), pos.getY(), pos.getZ(), oldItems.get(i).getItemStack());
            }
            oldItems = null;
        }
    }

//    @Override
//    public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state) {
//        if (state.get(TYPE).isDouble()) {
//
//            int count = 0;
//            while (oldItems == null || count > 1000) {
//                System.out.println("Waiting...");
//                count++;
//            }
//
//            world.setBlockState(pos, state.with(TYPE, CrateType.DEFAULT), 2);
//            CrateTileEntity newTile = (CrateTileEntity) world.getTileEntity(pos);
//
//            if (newTile != null) {
//                for (int i = 0; i < 6; i++) {
//
//                    if (!world.isRemote()) {
//                        InventoryHelper.spawnItemStack((World) world, pos.getX(), pos.getY(), pos.getZ(), oldItems.get(i + 6).getItemStack());
//                    }
//
//                    newTile.getItems().set(i, oldItems.get(i));
//                }
//
//                newTile.markDirty();
//            }
//
//            oldItems = null;
//        }
//    }
//
//    private void convertToSingle(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving, CrateTileEntity tile) {
//        // Spawn CrateBlockItem
//        InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Item.getItemFromBlock(this)));
//
//        // Spawn inventory of top contents
//        for (int i = 6; i < tile.getSizeInventory(); i++) {
//            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.getStackInSlot(i));
//            tile.setInventorySlotContents(i, ItemStack.EMPTY);
//        }
//        super.onReplaced(state, world, pos, newState, isMoving);
//        world.setBlockState(pos, state.with(TYPE, CrateType.DEFAULT), 2);
//
//        System.out.println(world.getTileEntity(pos) == tile);
//    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        CrateType crateType = state.get(TYPE);
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!crateType.isDouble() && player.isCreative() && heldItem.getItem() == inclinable) {
            world.setBlockState(pos, state.with(TYPE, crateType.opposite()));
            return ActionResultType.CONSUME;
        }

        if (!world.isRemote) {

            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof CrateTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (CrateTileEntity) tile, tile.getPos());
            }
            return ActionResultType.CONSUME;
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        CrateType crateType = state.get(TYPE);
        ItemStack heldItem = player.getHeldItemMainhand();

        if (!player.isCreative() && !crateType.isDouble() && hasProperItem(state, crateType, heldItem)) {

            SoundEvent blockSound = SoundEvents.BLOCK_WOOD_BREAK;
            if (crateType == CrateType.DEFAULT) {
                heldItem.setCount(heldItem.getCount() - 1);
                blockSound = SoundEvents.BLOCK_WOOD_PLACE;
            } else {
                player.dropItem(new ItemStack(inclinable), false);
            }

            playSound(world, pos, player, blockSound);
            world.setBlockState(pos, state.with(TYPE, crateType.opposite()));
        }
    }

    public void playSound(World world, BlockPos pos, PlayerEntity player, SoundEvent blockSound) {
        world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), blockSound, SoundCategory.BLOCKS, 0.8f, 1.5f);
    }

    private boolean hasProperTool(BlockState state, ItemStack heldItem) {
        return heldItem.getItem().getToolTypes(ItemStack.EMPTY).contains(this.getHarvestTool(state));
    }

    private boolean hasProperItem(BlockState state, CrateType type, ItemStack heldItem) {
        if (type == CrateType.INCLINED) {
            return hasProperTool(state, heldItem);
        }
        return heldItem.getItem() == inclinable && heldItem.getCount() >= 1;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrateTileEntity();
    }

}