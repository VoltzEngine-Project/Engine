package universalelectricity.core.electricity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IConductor;

public class ConductorRegistry implements IConductorRegistry
{
	public static final IConductorRegistry INSTANCE = new ConductorRegistry();
	public static final List<IConductor> conductors = new ArrayList<IConductor>();

	static
	{
		UniversalElectricity.CONDUCTOR_REGISTRY = INSTANCE;
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	@Override
	public void register(IConductor conductor)
	{
		this.cleanConductors();

		if (!this.conductors.contains(conductor))
		{
			this.conductors.addAll(Arrays.asList(conductor));
		}
	}

	@Override
	public void cleanConductors()
	{
		Iterator it = this.conductors.iterator();

		while (it.hasNext())
		{
			IConductor conductor = (IConductor) it.next();

			if (conductor == null)
			{
				it.remove();
			}
			else if (((TileEntity) conductor).isInvalid())
			{
				it.remove();
			}
		}
	}

	@Override
	public void resetAllConnections()
	{
		this.cleanConductors();
		try
		{
			Iterator<IConductor> it = this.conductors.iterator();

			while (it.hasNext())
			{
				IConductor conductor = it.next();

				conductor.setNetwork(new ElectricityNetwork(conductor));
				conductor.refreshConnectedBlocks(false);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public List<IConductor> getConductors()
	{
		return this.conductors;
	}

	@ForgeSubscribe
	public void onWorldUnLoad(WorldEvent.Unload event)
	{
		INSTANCE.getConductors().clear();
	}

	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{
		INSTANCE.getConductors().clear();
	}
}
