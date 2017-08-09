package com.builtbroken.mc.core.content.blast.emp;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.BlockEditResult;
import com.builtbroken.mc.api.energy.IEMReceptiveDevice;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import net.minecraft.tileentity.TileEntity;

/** Version of block edit that doesn't remove a block but instead removes it's power */
public class EmpEdit extends BlockEdit
{
    final double power;
    final double distance;
    final BlastEMP source;

    public EmpEdit(IWorldPosition vec, BlastEMP source,  double distance, double power)
    {
        super(vec);
        this.source = source;
        this.distance = distance;
        this.power = power;
    }

    @Override
    public boolean hasChanged()
    {
        //return true as we have no way of really checking
        // if power levels changed between first and final, effectively...
        return true;
    }

    @Override
    public BlockEditResult place()
    {
        if (world != null) //TODO add event fires
        {
            if (isChunkLoaded())
            {
                return doPlace();
            }
            return BlockEditResult.CHUNK_UNLOADED;
        }
        return BlockEditResult.NULL_WORLD;
    }

    @Override
    protected BlockEditResult doPlace()
    {
        TileEntity tile = getTileEntity();
        if (tile instanceof IEMReceptiveDevice)
        {
            ((IEMReceptiveDevice) tile).onElectromagneticRadiationApplied(source, distance, power, true);
            return BlockEditResult.PLACED;
        }
        return BlockEditResult.PREV_BLOCK_CHANGED;
    }
}