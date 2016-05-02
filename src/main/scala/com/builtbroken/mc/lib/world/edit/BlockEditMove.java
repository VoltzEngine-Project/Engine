package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.lib.transform.vector.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Version of the block edit that is designed to move a block from one pos to another. While maintaining all properities about the block
 * during and after it's movement.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2016.
 */
public class BlockEditMove extends BlockEdit
{
    /** Position */
    public Location newMovementLocation;

    public BlockEditMove(NBTTagCompound nbt)
    {
        super(nbt);
        if (nbt.hasKey("newPos"))
        {
            newMovementLocation = new Location(nbt.getCompoundTag("newPos"));
        }
    }

    public BlockEditMove(IWorldPosition vec, IWorldPosition newPos)
    {
        super(vec);
        logPrevBlock();
    }

    @Override
    protected BlockEditResult doPlace()
    {
        if (newMovementLocation != null)
        {
            TileEntity entity = getTileEntity();
            NBTTagCompound tag = new NBTTagCompound();
            if (entity != null)
            {
                entity.writeToNBT(tag);
                tag.removeTag("x");
                tag.removeTag("y");
                tag.removeTag("z");
                tag.removeTag("id");
            }
            BlockEditResult result = super.doPlace();
            if (result == BlockEditResult.PLACED)
            {
                newMovementLocation.setBlock(prev_block, prev_meta);
                TileEntity tile = newMovementLocation.getTileEntity();
                if (tile != null)
                {
                    tile.readFromNBT(tag);
                    tile.xCoord = newMovementLocation.xi();
                    tile.yCoord = newMovementLocation.yi();
                    tile.zCoord = newMovementLocation.zi();
                }
            }
            return result;
        }
        return BlockEditResult.INVALID;
    }

    @Override
    public NBTTagCompound writeNBT(NBTTagCompound nbt)
    {
        super.writeNBT(nbt);
        if (newMovementLocation != null)
        {
            nbt.setTag("newPos", newMovementLocation.toNBT());
        }
        return nbt;
    }
}
