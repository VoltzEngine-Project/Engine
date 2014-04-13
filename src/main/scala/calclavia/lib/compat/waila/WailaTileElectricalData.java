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

import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;

/**
 * @author tgame14
 * @since 12/04/14
 */
public class WailaTileElectricalData implements IWailaDataProvider
{
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

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		TileEntity tile = accessor.getTileEntity();
		if (!(tile instanceof TileElectrical))
		{
			return currenttip;
		}
		TileElectrical te = (TileElectrical) tile;
		currenttip.add(LanguageUtility.getLocal("info.energylevel.waila") + " " + UnitDisplay.getDisplay(te.getEnergyHandler().getEnergy(), Unit.JOULES));
		currenttip.add(LanguageUtility.getLocal("info.energycapacity.waila") + " " + UnitDisplay.getDisplay(te.getEnergyHandler().getEnergyCapacity(), Unit.JOULES));

		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}
}
