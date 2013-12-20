package universalelectricity.core.grid;

import java.util.Arrays;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.energy.IEnergyConductor;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorHelper;
import universalelectricity.core.electricity.ElectricalEvent.EnergyUpdateEvent;

/**
 * @author Calclavia
 * 
 */
public class EnergyNetwork extends Network<IEnergyNetwork, IEnergyConductor, TileEntity> implements IEnergyNetwork
{
	private float cachedResistance = 0;

	@Override
	public void update()
	{
		int totalEnergy = 0;

		for (IEnergyConductor conductor : this.getConnectors())
		{
			totalEnergy += conductor.onExtractEnergy(null, conductor.getEnergyCapacitance(), true);
		}

		int energyLoss = getTotalEnergyLoss();
		int totalUsableEnergy = totalEnergy - energyLoss;
		int remainingUsableEnergy = totalUsableEnergy;

		int toDistribute = (totalUsableEnergy / this.getNodes().size());
		int toDistributeMaster = (toDistribute + totalUsableEnergy % this.getNodes().size());
		int sideDistribute = (sideDistribute / 6);
		int sideDistributeMaster = (sideDistributeMaster / 6);

		EnergyUpdateEvent evt = new EnergyUpdateEvent(this);
		MinecraftForge.EVENT_BUS.post(evt);

		if (!evt.isCanceled())
		{
			for (TileEntity tileEntity : this.getNodes())
			{
				if (tileEntity instanceof IEnergyInterface)
				{
					IEnergyInterface electricalTile = (IEnergyInterface) tileEntity;

					for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
					{
						if (electricalTile.canConnect(direction) && this.getConnectors().contains(VectorHelper.getConnectorFromSide(tileEntity.worldObj, new Vector3(tileEntity), direction)))
						{
							int energyToSend = (int) (totalUsableEnergy * (electricalTile.getRequest(direction) / totalEnergyRequest));

							if (energyToSend > 0)
							{
								remainingUsableEnergy -= ((IEnergyInterface) tileEntity).onReceiveEnergy(direction, energyToSend, true);
							}
						}
					}
				}
			}
		}
	}

	public int getTotalEnergyLoss()
	{
		int loss = 0;

		for (IEnergyConductor conductor : this.getConnectors())
		{
			loss += conductor.getEnergyLoss();
		}

		return loss;
	}
}
