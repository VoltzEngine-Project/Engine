package com.builtbroken.mc.framework.multiblock.types;

import cofh.api.energy.IEnergyHandler;
import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.ITileConnection;
import com.builtbroken.mc.framework.multiblock.TileMulti;
import com.builtbroken.mc.framework.energy.UniversalEnergySystem;
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
public class TileMultiEnergy extends TileMulti implements IEnergyBufferProvider, ITileConnection, IEnergyHandler
{
    @Override
    public boolean canConnect(TileEntity connection, ConnectionType type, ForgeDirection from)
    {
        if (getHost() instanceof ITileConnection)
        {
            //TODO implement multi-block support for connection types and support for exact side checks
            return ((ITileConnection) getHost()).canConnect(connection, type, from);
        }
        return type == ConnectionType.POWER && getHost() instanceof IEnergyBufferProvider;
    }

    @Override
    public boolean hasConnection(ConnectionType type, ForgeDirection side)
    {
        if (getHost() instanceof ITileConnection)
        {
            //TODO implement multi-block support for connection types and support for exact side checks
            return ((ITileConnection) getHost()).hasConnection(type, side);
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

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        IEnergyBuffer buffer = getEnergyBuffer(from);
        if (buffer != null)
        {
            int received = buffer.addEnergyToStorage(UniversalEnergySystem.RF_HANDLER.toUEEnergy(maxReceive), !simulate);
            return UniversalEnergySystem.RF_HANDLER.fromUE(received);
        }
        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        IEnergyBuffer buffer = getEnergyBuffer(from);
        if (buffer != null)
        {
            int extracted = buffer.removeEnergyFromStorage(UniversalEnergySystem.RF_HANDLER.toUEEnergy(maxExtract), !simulate);
            return UniversalEnergySystem.RF_HANDLER.fromUE(extracted);
        }
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        IEnergyBuffer buffer = getEnergyBuffer(from);
        if (buffer != null)
        {
            return UniversalEnergySystem.RF_HANDLER.fromUE(buffer.getEnergyStored());
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        IEnergyBuffer buffer = getEnergyBuffer(from);
        if (buffer != null)
        {
            return UniversalEnergySystem.RF_HANDLER.fromUE(buffer.getMaxBufferSize());
        }
        return 0;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        ForgeDirection dir = from;
        TileEntity connector = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
        if (connector != null)
        {
            return this.canConnect(connector, ConnectionType.RF_POWER, from) || this.canConnect(connector, ConnectionType.POWER, from);
        }
        return false;
    }
}
