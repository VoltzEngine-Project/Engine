package com.builtbroken.mc.lib.mod.compat.ic;

import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.core.asm.template.ITemplateCalls;
import cpw.mods.fml.common.FMLCommonHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.info.Info;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Handles IC2 related code for ASM injected methods, prevents ASM errors.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/11/2016.
 */
public class ICStaticForwarder implements ITemplateCalls
{
    public static double getDemandedEnergy(IEnergyBufferProvider buffer)
    {
        return buffer.getEnergyBuffer(null) != null ? buffer.getEnergyBuffer(null).getMaxBufferSize() - buffer.getEnergyBuffer(null).getEnergyStored() : 0;
    }

    public static int getSinkTier(IEnergyBufferProvider buffer)
    {
        return Integer.MAX_VALUE;
    }

    public static double injectEnergy(IEnergyBufferProvider buffer, ForgeDirection directionFrom, double amount, double voltage)
    {
        return 0;
    }

    public static boolean acceptsEnergyFrom(IEnergyBufferProvider buffer, TileEntity emitter, ForgeDirection direction)
    {
        return true; //TODO add connection interface
    }

    public void load(Object sink)
    {
        if (sink instanceof IEnergySink && !FMLCommonHandler.instance().getEffectiveSide().isClient() && Info.isIc2Available())
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergySink) sink));
        }
    }

    public void unload(Object sink)
    {
        if (sink instanceof IEnergySink && Info.isIc2Available())
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergySink) sink));
        }
    }
}
