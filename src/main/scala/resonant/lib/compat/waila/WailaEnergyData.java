package resonant.lib.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import resonant.api.IIO;
import resonant.lib.prefab.tile.TileElectrical;
import resonant.lib.util.LanguageUtility;
import universalelectricity.api.UnitDisplay;
import universalelectricity.api.core.grid.electric.IEnergyContainer;

import java.util.List;

/**
 * Waila support for Electrical tiles
 *
 * @author tgame14, DarkGuardsman
 */
public class WailaEnergyData implements IWailaDataProvider
{
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		TileEntity tile = accessor.getTileEntity();
		boolean input = true, output = true;
		currenttip.add("" + accessor.getSide());
		if (tile instanceof IIO)
		{
			IIO te = (IIO) tile;
			input = false;
			output = false;
			if (te.getInputDirections() != null)
			{
				input = te.getInputDirections().contains(accessor.getSide().getOpposite());
			}
			if (te.getOutputDirections() != null)
			{
				output = te.getOutputDirections().contains(accessor.getSide().getOpposite());
			}
		}
		//Energy support
		if (tile instanceof TileElectrical)
		{
			TileElectrical te = (TileElectrical) tile;
			currenttip.add(LanguageUtility.getLocal("info.waila.energy") + " " + UnitDisplay.getDisplayShort(te.getEnergyHandler().getEnergy(), UnitDisplay.Unit.JOULES) + " / " + UnitDisplay.getDisplayShort(te.getEnergyHandler().getEnergyCapacity(), UnitDisplay.Unit.JOULES));
		}
		else if ((input || output) && tile instanceof IEnergyContainer)
		{
			IEnergyContainer te = (IEnergyContainer) tile;
			currenttip.add(LanguageUtility.getLocal("info.waila.energy") + " " + UnitDisplay.getDisplayShort(te.getEnergy(accessor.getSide()), UnitDisplay.Unit.JOULES) + " / " + UnitDisplay.getDisplayShort(te.getEnergyCapacity(accessor.getSide()), UnitDisplay.Unit.JOULES));
		}/*
		//Voltage support
		if (input && tile instanceof IVoltageInput)
		{
			IVoltageInput te = (IVoltageInput) tile;
			currenttip.add(LanguageUtility.getLocal("info.waila.voltage.in") + " " + UnitDisplay.getDisplayShort(te.getVoltageInput(accessor.getSide()), UnitDisplay.Unit.VOLTAGE));
		}
		if (output && tile instanceof IVoltageOutput)
		{
			IVoltageOutput te = (IVoltageOutput) tile;
			currenttip.add(LanguageUtility.getLocal("info.waila.voltage.out") + " " + UnitDisplay.getDisplayShort(te.getVoltageOutput(accessor.getSide()), UnitDisplay.Unit.VOLTAGE));
		}
		//Wire support
		if (tile instanceof IConductor)
		{
			IConductor te = (IConductor) tile;
			currenttip.add(LanguageUtility.getLocal("info.waila.amp") + " " + UnitDisplay.getDisplayShort(te.getCurrentCapacity(), UnitDisplay.Unit.AMPERE));
			currenttip.add(LanguageUtility.getLocal("info.waila.ohm") + " " + UnitDisplay.getDisplayShort(te.getResistance(), UnitDisplay.Unit.RESISTANCE));
		}*/
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
