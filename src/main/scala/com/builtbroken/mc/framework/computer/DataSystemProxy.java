package com.builtbroken.mc.framework.computer;

import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/20/2018.
 */
public class DataSystemProxy extends AbstractLoadable
{
    @Override
    public void preInit()
    {
        DataSystemHandler.addSharedMethod("getEnergyStored", tile -> {
            if (tile instanceof IEnergyBufferProvider)
            {
                return (host, method, args) -> {
                    if (host instanceof IEnergyBufferProvider)
                    {
                        IEnergyBuffer buffer = ((IEnergyBufferProvider) host).getEnergyBuffer(ForgeDirection.UNKNOWN);
                        if (buffer != null)
                        {
                            return new Object[]{buffer.getEnergyStored()};
                        }
                        return new Object[]{"Error: Object returned no energy data"};
                    }
                    return new Object[]{"Error: Object is not an energy storage"};
                };
            }
            return null;
        });

        DataSystemHandler.addSharedMethod("getEnergyCapacity", tile -> {
            if (tile instanceof IEnergyBufferProvider)
            {
                return (host, method, args) -> {
                    if (host instanceof IEnergyBufferProvider)
                    {
                        IEnergyBuffer buffer = ((IEnergyBufferProvider) host).getEnergyBuffer(ForgeDirection.UNKNOWN);
                        if (buffer != null)
                        {
                            return new Object[]{buffer.getMaxBufferSize()};
                        }
                        return new Object[]{"Error: Object returned no energy data"};
                    }
                    return new Object[]{"Error: Object is not an energy storage"};
                };
            }
            return null;
        });
    }
}
