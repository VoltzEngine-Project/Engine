package com.builtbroken.mc.prefab.tile.multiblock.types;

import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.ITileConnection;
import com.builtbroken.mc.prefab.tile.multiblock.TileMulti;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Implementation of multi-block that can accept energy
 * <p>
 * Works as a wrapper for the host block
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/17/2017.
 */
public class TileMultiEnergy extends TileMulti implements IEnergyBufferProvider, ITileConnection
{
    @Override
    public boolean canConnect(TileEntity connection, ConnectionType type, ForgeDirection from)
    {
        if (type == ConnectionType.POWER && getHost() instanceof IEnergyBufferProvider)
        {
            //TODO implement multi-block support for connection types and support for exact side checks
            return !(getHost() instanceof ITileConnection) || ((ITileConnection) getHost()).canConnect(connection, type, from);
        }
        return false;
    }

    @Override
    public boolean hasConnection(ConnectionType type, ForgeDirection side)
    {
        if (type == ConnectionType.POWER && getHost() instanceof IEnergyBufferProvider)
        {
            //TODO implement multi-block support for connection types and support for exact side checks
            return !(getHost() instanceof ITileConnection) || ((ITileConnection) getHost()).hasConnection(type, side);
        }
        return false;
    }

    @Override
    public IEnergyBuffer getEnergyBuffer(ForgeDirection side)
    {
        if (getHost() instanceof IEnergyBufferProvider)
        {
            //TODO implement multi-block support for exact side checks
            return ((IEnergyBufferProvider) getHost()).getEnergyBuffer(side);
        }
        return null;
    }
}
