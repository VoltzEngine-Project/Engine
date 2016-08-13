package com.builtbroken.mc.lib.mod.compat.ic;

import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.api.event.tile.TileEvent;
import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.ITileConnection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
public class ICStaticForwarder
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
        if (buffer.getEnergyBuffer(directionFrom) != null)
        {
            //TODO Check for rounding error causing net power gains
            return buffer.getEnergyBuffer(directionFrom).addEnergyToStorage((int) (amount * ICHandler.TO_UE), true) * ICHandler.FROM_UE;
        }
        return 0;
    }

    public static boolean acceptsEnergyFrom(IEnergyBufferProvider buffer, TileEntity emitter, ForgeDirection direction)
    {
        return !(buffer instanceof ITileConnection) || ((ITileConnection) buffer).canConnect(emitter, ConnectionType.IC_POWER, direction);
    }

    public static final ICStaticForwarder INSTANCE = new ICStaticForwarder();

    private ICStaticForwarder()
    {

    }

    @SubscribeEvent
    public void load(TileEvent.TileLoadEvent event)
    {
        TileEntity sink = event.tile();
        if (sink instanceof IEnergySink && !FMLCommonHandler.instance().getEffectiveSide().isClient() && Info.isIc2Available())
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergySink) sink));
        }
    }

    @SubscribeEvent
    public void unload(TileEvent.TileUnLoadEvent event)
    {
        TileEntity sink = event.tile();
        if (sink instanceof IEnergySink && Info.isIc2Available())
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergySink) sink));
        }
    }
}
