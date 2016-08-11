package com.builtbroken.mc.lib.mod.compat.ic;

import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.prefab.tile.Tile;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/11/2016.
 */
public abstract class ICTemplateTile extends Tile implements IEnergySink, IEnergyBufferProvider
{
    @Override
    public double getDemandedEnergy()
    {
        return ICStaticForwarder.getDemandedEnergy(this);
    }

    @Override
    public int getSinkTier()
    {
        return ICStaticForwarder.getSinkTier(this);
    }

    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
    {
        return ICStaticForwarder.injectEnergy(this, directionFrom, amount, voltage);
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
    {
        return ICStaticForwarder.acceptsEnergyFrom(this, emitter, direction);
    }
}
