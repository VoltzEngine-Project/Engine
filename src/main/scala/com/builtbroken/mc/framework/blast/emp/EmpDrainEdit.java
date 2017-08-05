package com.builtbroken.mc.framework.blast.emp;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.BlockEditResult;
import com.builtbroken.mc.lib.energy.UniversalEnergySystem;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import net.minecraft.tileentity.TileEntity;

/** Version of block edit that doesn't remove a block but instead removes it's power */
public class EmpDrainEdit extends BlockEdit
{
    final double power;

    public EmpDrainEdit(IWorldPosition vec, double power)
    {
        super(vec);
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
        //TODO destroy machines, replace machine with burnt up machine containing 60% of crafting parts
        //We can not place a block without a world
        if (world != null)
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
        if (tile != null && UniversalEnergySystem.isHandler(tile, null))
        {
            //TODO get accessible sides to improve compatibility and performance
            //TODO make a simple drain method
            //TODO fire events
            //TODO implement EMP api to allow machines to control, and config overrides to bypass control
            //TODO add continues drain effect
            //TODO break settings on machines
            //final double power = UniversalEnergySystem.extractEnergy(tile, UniversalEnergySystem.getPotentialEnergy(tile), false);
            //final double powerScaled = power * power;
            //UniversalEnergySystem.extractEnergy(tile, powerScaled, true);
            UniversalEnergySystem.clearEnergy(tile, true);
            return BlockEditResult.PLACED;
        }
        return BlockEditResult.PREV_BLOCK_CHANGED;
    }
}