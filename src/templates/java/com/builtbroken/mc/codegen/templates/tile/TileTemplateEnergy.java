package com.builtbroken.mc.codegen.templates.tile;

import cofh.api.energy.IEnergyHandler;
import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.ITileConnection;
import com.builtbroken.mc.api.tile.node.ITileNode;
import com.builtbroken.mc.codegen.processor.TileWrappedTemplate;
import com.builtbroken.mc.framework.energy.UniversalEnergySystem;
import com.builtbroken.mc.seven.framework.logic.TileEntityWrapper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
@TileWrappedTemplate(annotationName = "EnergyWrapped")
public class TileTemplateEnergy extends TileEntityWrapper implements IEnergyBufferProvider, IEnergyHandler
{
    public TileTemplateEnergy(ITileNode controller)
    {
        super(controller);
    }

    //#StartMethods#
    @Override
    public IEnergyBuffer getEnergyBuffer(ForgeDirection side)
    {
        if (getTileNode() instanceof IEnergyBufferProvider)
        {
            return ((IEnergyBufferProvider) getTileNode()).getEnergyBuffer(side);
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
        if (getTileNode() instanceof ITileConnection)
        {
            TileEntity connector = toPos().add(from).getTileEntity(worldObj);
            if (((ITileConnection) getTileNode()).canConnect(connector, ConnectionType.RF_POWER, from))
            {
                return true;
            }
            else if (((ITileConnection) getTileNode()).canConnect(connector, ConnectionType.POWER, from))
            {
                return true;
            }
            return false;
        }
        return true;
    }
    //#EndMethods#
}
