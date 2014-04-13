package calclavia.lib.compat.waila;

import calclavia.lib.prefab.tile.TileElectrical;
import calclavia.lib.utility.LanguageUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.electricity.IVoltageOutput;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;

/** Waila support for Electrical tiles
 * 
 * @author tgame14, DarkGuardsman */
public class WailaEnergyData implements IWailaDataProvider
{
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        TileEntity tile = accessor.getTileEntity();
        //Energy support
        if (tile instanceof TileElectrical)
        {
            TileElectrical te = (TileElectrical) tile;
            currenttip.add(LanguageUtility.getLocal("info.waila.energy") + " " + UnitDisplay.getDisplayShort(te.getEnergyHandler().getEnergy(), Unit.JOULES) + " / " + UnitDisplay.getDisplayShort(te.getEnergyHandler().getEnergyCapacity(), Unit.JOULES));
        }
        else if (tile instanceof IEnergyContainer)
        {
            IEnergyContainer te = (IEnergyContainer) tile;
            currenttip.add(LanguageUtility.getLocal("info.waila.energy") + " " + UnitDisplay.getDisplayShort(te.getEnergy(accessor.getSide()), Unit.JOULES) + " / " + UnitDisplay.getDisplayShort(te.getEnergyCapacity(accessor.getSide()), Unit.JOULES));
        }
        //Voltage support
        if(tile instanceof IVoltageInput)
        {
            IVoltageInput te = (IVoltageInput) tile;
            currenttip.add(LanguageUtility.getLocal("info.waila.voltage.in") + " " + UnitDisplay.getDisplayShort(te.getVoltageInput(accessor.getSide()), Unit.VOLTAGE));
        }
        if(tile instanceof IVoltageOutput)
        {
            IVoltageOutput te = (IVoltageOutput) tile;
            currenttip.add(LanguageUtility.getLocal("info.waila.voltage.out") + " " + UnitDisplay.getDisplayShort(te.getVoltageOutput(accessor.getSide()), Unit.VOLTAGE));
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }
}
