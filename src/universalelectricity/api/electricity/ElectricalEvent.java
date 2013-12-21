package universalelectricity.api.electricity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.energy.IEnergyNetwork;
import universalelectricity.core.electricity.ElectricityPack;

public class ElectricalEvent extends Event
{
	/**
	 * Call this to have your TileEntity produce power into the network.
	 * 
	 * @author Calclavia
	 * 
	 */
	@Cancelable
	public static class ElectricityProduceEvent extends ElectricalEvent
	{
		public World world;
		public IEnergyInterface tileEntity;

		public ElectricityProduceEvent(IEnergyInterface tileEntity)
		{
			this.tileEntity = tileEntity;
			this.world = ((TileEntity) this.tileEntity).worldObj;
		}
	}

	public static class NetworkEvent extends ElectricalEvent
	{
		public final IEnergyNetwork network;
		public ElectricityPack electricityPack;
		public TileEntity[] ignoreTiles;

		public NetworkEvent(IEnergyNetwork network, ElectricityPack electricityPack, TileEntity... ignoreTiles)
		{
			this.network = network;
			this.electricityPack = electricityPack;
			this.ignoreTiles = ignoreTiles;
		}
	}

	/**
	 * Internal Events. These events are fired when something happens in the network.
	 * 
	 * @author Calclavia
	 * 
	 */
	@Cancelable
	public static class EnergyUpdateEvent extends Event
	{
		public final IEnergyNetwork network;

		public EnergyUpdateEvent(IEnergyNetwork network)
		{
			this.network = network;
		}
	}

	public static class ElectricityRequestEvent extends NetworkEvent
	{
		public ElectricityRequestEvent(IEnergyNetwork network, ElectricityPack electricityPack, TileEntity... ignoreTiles)
		{
			super(network, electricityPack, ignoreTiles);
		}
	}

}
