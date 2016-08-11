package com.builtbroken.mc.lib.mod.compat.rf;

import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.ITileConnection;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Used to prevent issues with ASM methods holding into references of the template class file
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/9/2016.
 */
public class RFStaticForwarder
{
    public static int receiveEnergy(IEnergyBufferProvider provider, ForgeDirection from, int maxReceive, boolean simulate)
    {
        if (provider.getEnergyBuffer(from) != null)
        {
            double ue = maxReceive * RFEnergyHandler.TO_UE_FROM_RF;
            int rf = (int) (provider.getEnergyBuffer(from).addEnergyToStorage((int) ue, !simulate) * RFEnergyHandler.TO_RF_FROM_UE);
            if (rf == 0 && ue > 0)
            {
                rf = 1;
            }
            return rf;
        }
        return 0;
    }

    public static int extractEnergy(IEnergyBufferProvider provider, ForgeDirection from, int maxExtract, boolean simulate)
    {
        //TODO ensure rounding errors do not result in gain of energy
        return provider.getEnergyBuffer(from) != null ? (int) (provider.getEnergyBuffer(from).removeEnergyFromStorage((int) (maxExtract * RFEnergyHandler.TO_UE_FROM_RF), !simulate) * RFEnergyHandler.TO_RF_FROM_UE) : 0;
    }

    /**
     * Returns the amount of energy currently stored.
     */

    public static int getEnergyStored(IEnergyBufferProvider provider, ForgeDirection from)
    {
        return provider.getEnergyBuffer(from) != null ? (int) (provider.getEnergyBuffer(from).getEnergyStored() * RFEnergyHandler.TO_RF_FROM_UE) : 0;
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     */

    public static int getMaxEnergyStored(IEnergyBufferProvider provider, ForgeDirection from)
    {
        return provider.getEnergyBuffer(from) != null ? (int) (provider.getEnergyBuffer(from).getMaxBufferSize() * RFEnergyHandler.TO_RF_FROM_UE) : 0;
    }


    public static boolean canConnectEnergy(IEnergyBufferProvider provider, ForgeDirection from)
    {
        TileEntity tile = new Pos((TileEntity) provider).sub(from).getTileEntity(((TileEntity) provider).getWorldObj()); //TODO check that this is the right tile
        return !(provider instanceof ITileConnection) || ((ITileConnection) provider).canConnect(tile, ConnectionType.RF_POWER, from);
    }
}
