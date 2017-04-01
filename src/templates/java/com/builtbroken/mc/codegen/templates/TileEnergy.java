package com.builtbroken.mc.codegen.templates;

import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.codegen.template.TileWrappedTemplate;
import com.builtbroken.mc.framework.logic.ITileNode;
import com.builtbroken.mc.framework.logic.wrapper.TileEntityWrapper;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
@TileWrappedTemplate(annotationName = "EnergyWrapped")
public class TileEnergy extends TileEntityWrapper implements IEnergyBufferProvider
{
    public TileEnergy(ITileNode controller)
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
    //#EndMethods#
}
