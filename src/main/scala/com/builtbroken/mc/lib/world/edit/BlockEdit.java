package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.BlockEditResult;
import com.builtbroken.mc.api.explosive.IBlastEdit;
import com.builtbroken.mc.imp.transform.vector.AbstractLocation;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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
    public Block prev_block = Blocks.air;
    /** 0-15 meta that was at the location */
    public int prev_meta = 0;

    /** Cached to get drops at time of call */
    public List<ItemStack> drops;

    /** Block to place */
    public Block newBlock = Blocks.air;
    /** 0-15 meta value to place */
    public int newMeta = 0;

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
        set(block, 0, false, true);
    }

    public BlockEdit(IWorldPosition vec, Block block, int meta)
    {
        super(vec);
        set(block, meta, false, true);
    }

    public BlockEdit(World world, IPos3D vector)
    {
        super(world, vector);
    }

    public BlockEdit(World world, Vec3 vec)
    {
        super(world, vec);
    }

    public BlockEdit(World world, MovingObjectPosition target)
    {
        super(world, target);
    }

    /**
     * Sets placement data and additional checks
     *
     * @param block
     * @param meta
     * @param doDrop
     * @param checkEquals
     * @return
     */
    public BlockEdit set(Block block, int meta, boolean doDrop, boolean checkEquals)
    {
        this.newBlock = block;
        this.newMeta = meta;
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
        return set(block, 0, false, true);
    }

    /**
     * Sets placement data
     *
     * @param block
     * @return this
     */
    public BlockEdit set(Block block, int meta)
    {
        return set(block, meta, false, true);
    }

    /**
     * Sets placement data to air
     *
     * @return this
     */
    public BlockEdit setAir()
    {
        return set(Blocks.air, 0, false, true);
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
            prev_block = getBlock();
            prev_meta = getBlockMetadata();
        }
        return this;
    }

    @Override
    public AxisAlignedBB getBounds()
    {
        if (bounds == null)
        {
            this.bounds = AxisAlignedBB.getBoundingBox(xi(), yi(), zi(), xi() + 1, yi() + 1, zi() + 1);
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
            Chunk chunk = world.getChunkFromBlockCoords(xi(), zi());
            if (chunk != null && chunk.isChunkLoaded)
            {
                //Check if the prev_block still exists
                if (checkForPrevBlockEquals && prev_block != getBlock() && prev_meta != getBlockMetadata())
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
        if (getBlock() == newBlock && getBlockMetadata() == newMeta)
        {
            return BlockEditResult.ALREADY_PLACED;
        }
        //Breaks block in order to drop items contained
        if (doItemDrop)
        {
            InventoryUtility.dropBlockAsItem(world, xi(), yi(), zi(), true);
        }
        //Place the block and check if the world says its placed
        if (super.setBlock(world, newBlock, newMeta, placementNotification))
        {
            //Checks if the block can stay to fix block issues (crops, plants, doors, plates, redstone)
            if (!newBlock.canBlockStay(world, xi(), yi(), zi()))
            {
                //Drops the block
                InventoryUtility.dropBlockAsItem(world, xi(), yi(), zi(), true);
            }
            //Check to make blocks above this block are removed if invalid
            Block block = world.getBlock(xi(), yi() + 1, zi());
            if (!block.canBlockStay(world, xi(), yi() + 1, zi()))
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
            return getBlock().getDrops(world, xi(), yi(), zi(), getBlockMetadata(), f);
        }
        return new ArrayList();
    }

    @Override
    public boolean hasChanged()
    {
        return prev_block != newBlock || prev_meta != newMeta;
    }

    @Override
    public Block getNewBlock()
    {
        return newBlock;
    }

    @Override
    public int getNewMeta()
    {
        return newMeta;
    }

    @Override
    public int hashCode()
    {
        int result = 31 + (world() != null && world().provider != null ? world().provider.dimensionId : 0);
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
        return "BlockEdit[ " + (world() != null && world().provider != null ? world().provider.dimensionId : null) + "d, " + xi() + "x, " + yi() + "y, " + zi() + "z]";
    }

    public BlockEdit setNotificationLevel(int notificationlevel)
    {
        this.placementNotification = notificationlevel;
        return this;
    }
}
