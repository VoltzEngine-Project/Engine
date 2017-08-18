package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.BlockEditResult;
import com.builtbroken.mc.api.explosive.IBlastEdit;
import com.builtbroken.mc.imp.transform.vector.AbstractLocation;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

/**
 * World location containing data to set blocks
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/7/2015.
 */
public class BlockEdit extends AbstractLocation<BlockEdit> implements IBlastEdit, IPos3D, IWorldPosition
{
    /** Block that was at the location */
    public IBlockState prev_block = Blocks.AIR.getDefaultState();

    /** Cached to get drops at time of call */
    public List<ItemStack> drops;

    /** Block to place */
    public IBlockState newBlock = Blocks.AIR.getDefaultState();

    /** Force energy used to place it */
    public float energy = 0;
    /** direction placed from */
    public EnumFacing face = null;

    /** Drop block that was at the location */
    public boolean doItemDrop = false;
    /** Ensure prev_block is the same */
    public boolean checkForPrevBlockEquals = false;
    /** Ensure prev_block is the same */
    public boolean checkForEntity = false;

    public int placementNotification = 3;

    private AxisAlignedBB bounds;

    public BlockEdit(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public BlockEdit(NBTTagCompound nbt)
    {
        super(nbt);
    }

    public BlockEdit(ByteBuf data)
    {
        super(data);
    }

    public BlockEdit(Entity entity)
    {
        super(entity);
    }

    public BlockEdit(TileEntity tile)
    {
        super(tile);
    }

    public BlockEdit(IWorldPosition vec)
    {
        super(vec);
    }

    public BlockEdit(IWorldPosition vec, Block block)
    {
        super(vec);
        set(block.getDefaultState(), false, true);
    }

    public BlockEdit(IWorldPosition vec, IBlockState state)
    {
        super(vec);
        set(state, false, true);
    }

    public BlockEdit(World world, IPos3D vector)
    {
        super(world, vector);
    }

    public BlockEdit(World world, Vec3d vec)
    {
        super(world, vec);
    }

    public BlockEdit(World world, RayTraceResult target)
    {
        super(world, target);
    }

    /**
     * Sets placement data and additional checks
     *
     * @param state
     * @param doDrop
     * @param checkEquals
     * @return
     */
    public BlockEdit set(IBlockState state, boolean doDrop, boolean checkEquals)
    {
        this.newBlock = state;
        this.doItemDrop = doDrop;
        this.checkForPrevBlockEquals = checkEquals;
        logPrevBlock();
        return this;
    }

    /**
     * Sets placement data
     *
     * @param block
     * @return this
     */
    public BlockEdit set(Block block)
    {
        return set(block.getDefaultState(), false, true);
    }

    /**
     * Sets placement data
     *
     * @param state
     * @return this
     */
    public BlockEdit set(IBlockState state)
    {
        return set(state, false, true);
    }

    /**
     * Sets placement data to air
     *
     * @return this
     */
    public BlockEdit setAir()
    {
        return set(Blocks.AIR.getDefaultState(), false, true);
    }

    /**
     * Toggles blocks to drop during placement
     *
     * @return this
     */
    public void doDrops()
    {
        this.doItemDrop = true;
    }

    @Override
    public void setBlastDirection(EnumFacing dir)
    {
        this.face = dir;
    }

    @Override
    public EnumFacing getBlastDirection()
    {
        return face;
    }

    @Override
    public void setEnergy(float energy)
    {
        this.energy = energy;
    }

    @Override
    public float getEnergy()
    {
        return this.energy;
    }

    @Override
    public BlockEdit newPos(double x, double y, double z)
    {
        return new BlockEdit(world, x, y, z);
    }

    /**
     * Logs prev_block before running block placement.
     *
     * @return this
     */
    public BlockEdit logPrevBlock()
    {
        checkForPrevBlockEquals = true;
        if (world != null)
        {
            prev_block = getBlockState();
        }
        return this;
    }

    @Override
    public AxisAlignedBB getBounds()
    {
        if (bounds == null)
        {
            this.bounds = new AxisAlignedBB(xi(), yi(), zi(), xi() + 1, yi() + 1, zi() + 1);
        }
        return bounds;
    }

    @Override
    public BlockEditResult place()
    {
        //We can not place a block without a world
        if (world != null)
        {
            //Check if the chunk exists and is loaded to prevent loading/creating new chunks
            Chunk chunk = world.getChunkFromBlockCoords(toBlockPos());
            if (chunk != null && chunk.isLoaded())
            {
                //Check if the prev_block still exists
                IBlockState currentBlock = getBlockState();
                if (checkForPrevBlockEquals && prev_block != currentBlock)
                {
                    return BlockEditResult.PREV_BLOCK_CHANGED;
                }

                //Check if an entity is in the way
                if (checkForEntity)
                {
                    List entities = world.getEntitiesWithinAABB(Entity.class, getBounds());
                    if (entities.size() > 0)
                    {
                        return BlockEditResult.ENTITY_BLOCKED;
                    }
                }

                return doPlace();
            }
            return BlockEditResult.CHUNK_UNLOADED;
        }
        return BlockEditResult.NULL_WORLD;
    }

    /**
     * Called to place the block, override this for custom placement information
     *
     * @return result of placement
     */
    protected BlockEditResult doPlace()
    {
        //Check if it was already placed to prevent item lose if this is being used by a schematic
        if (getBlockState() == newBlock)
        {
            return BlockEditResult.ALREADY_PLACED;
        }
        //Breaks block in order to drop items contained
        if (doItemDrop)
        {
            InventoryUtility.dropBlockAsItem(world, xi(), yi(), zi(), false);
        }
        //Place the block and check if the world says its placed
        if (super.setBlock(world, newBlock, placementNotification))
        {
            //Checks if the block can stay to fix block issues (crops, plants, doors, plates, redstone)
            if (!newBlock.getBlock().canPlaceBlockAt(world, toBlockPos()))
            {
                //Drops the block
                InventoryUtility.dropBlockAsItem(world, xi(), yi(), zi(), true);
            }
            //Check to make blocks above this block are removed if invalid
            IBlockState block = world.getBlockState(toBlockPos().up());
            if (!block.getBlock().canPlaceBlockAt(world, toBlockPos().up()))
            {
                InventoryUtility.dropBlockAsItem(world, xi(), yi() + 1, zi(), true);
            }
            return BlockEditResult.PLACED;
        }
        return BlockEditResult.BLOCKED;
    }

    public List<ItemStack> getDrops()
    {
        return getDrops(0);
    }

    public List<ItemStack> getDrops(int f)
    {
        if (doItemDrop)
        {
            IBlockState state = getBlockState();
            NonNullList<ItemStack> items = NonNullList.create();
            state.getBlock().getDrops(items, world, toBlockPos(), state, f);
            return items;
        }
        return new ArrayList();
    }

    @Override
    public boolean hasChanged()
    {
        return prev_block != newBlock;
    }

    @Override
    public IBlockState getNewBlockState()
    {
        return newBlock;
    }


    @Override
    public int hashCode()
    {
        int result = 31 + (oldWorld() != null && oldWorld().provider != null ? oldWorld().provider.getDimension() : 0);
        result = 31 * result + xi();
        result = 31 * result + yi();
        result = 31 * result + zi();
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (obj instanceof BlockEdit)
        {
            return ((BlockEdit) obj).world == world
                    && ((BlockEdit) obj).xi() == xi()
                    && ((BlockEdit) obj).yi() == yi()
                    && ((BlockEdit) obj).zi() == zi();
        }
        return false;
    }

    public String toString()
    {
        return "BlockEdit[ " + (oldWorld() != null && oldWorld().provider != null ? oldWorld().provider.getDimension() : null) + "d, " + xi() + "x, " + yi() + "y, " + zi() + "z]";
    }

    public BlockEdit setNotificationLevel(int notificationlevel)
    {
        this.placementNotification = notificationlevel;
        return this;
    }
}
