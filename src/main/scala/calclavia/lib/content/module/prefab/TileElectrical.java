package calclavia.lib.content.module.prefab;

import calclavia.lib.utility.LanguageUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tgame14
 * @since 21/03/14
 */
@UniversalClass
public class TileElectrical extends calclavia.lib.prefab.tile.TileElectrical
		implements IEnergyContainer, IEnergyInterface, IWailaDataProvider
{
	public TileElectrical()
	{
		this(UniversalElectricity.machine);

	}

	public TileElectrical(Material material)
	{
		super(material);
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return new ItemStack(this.block());
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		List<String> list = new ArrayList<String>();
		list.add(LanguageUtility.getLocal("info.energylevel.waila") + " " + String.valueOf(this.getEnergy(ForgeDirection.UNKNOWN)));
		list.add(LanguageUtility.getLocal("info.energycapacity.waila") + " " + String.valueOf(this.getEnergyCapacity(ForgeDirection.UNKNOWN)));

		return list;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}
}
