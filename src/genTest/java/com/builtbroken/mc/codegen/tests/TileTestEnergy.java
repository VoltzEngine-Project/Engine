package com.builtbroken.mc.codegen.tests;

import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.framework.logic.TileNode;
import com.builtbroken.mc.framework.logic.annotations.EnergyWrapped;
import com.builtbroken.mc.framework.logic.annotations.TileWrapped;
import com.builtbroken.mc.prefab.energy.EnergyBuffer;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Test tile to see if {@link com.builtbroken.mc.codegen.Main} functions correctly for tile processing
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@TileWrapped(className = ".tile.TileEntityWrapperTestEnergy")
@EnergyWrapped()
public final class TileTestEnergy extends TileNode implements IEnergyBufferProvider
{
    protected EnergyBuffer buffer;

    @Override
    public IEnergyBuffer getEnergyBuffer(ForgeDirection side)
    {
        if(buffer == null)
        {
            buffer = new EnergyBuffer(10000);
        }
        return buffer;
    }
}
