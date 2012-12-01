package universalelectricity.core.electricity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.src.TileEntity;
import universalelectricity.core.implement.IConductor;

public class ElectricityNetwork
{
	private final HashMap<TileEntity, ElectricityPack> currentlyProducing = new HashMap<TileEntity, ElectricityPack>();
	private final HashMap<TileEntity, ElectricityPack> requests = new HashMap<TileEntity, ElectricityPack>();

	public final List<IConductor> conductors = new ArrayList<IConductor>();

	public ElectricityNetwork(IConductor conductor)
	{
		this.addConductor(conductor);
	}

	/**
	 * Sets this tile entity to start producing energy in this network.
	 */
	public void startProducing(TileEntity tileEntity, double amperes, double voltage)
	{
		if (tileEntity != null && amperes > 0 && voltage > 0)
		{
			if (this.currentlyProducing.containsKey(tileEntity))
			{
				this.currentlyProducing.get(tileEntity).amperes = amperes;
				this.currentlyProducing.get(tileEntity).voltage = voltage;
			}
			else
			{
				this.currentlyProducing.put(tileEntity, new ElectricityPack(amperes, voltage));
			}
		}
	}

	/**
	 * Sets this tile entity to stop producing energy in this network.
	 */
	public void stopProducing(TileEntity tileEntity)
	{
		this.currentlyProducing.remove(tileEntity);
	}
	
	/**
	 * Sets this tile entity to start producing energy in this network.
	 */
	public void startRequesting(TileEntity tileEntity, double amperes, double voltage)
	{
		if (tileEntity != null && amperes > 0 && voltage > 0)
		{
			if (this.requests.containsKey(tileEntity))
			{
				this.requests.get(tileEntity).amperes = amperes;
				this.requests.get(tileEntity).voltage = voltage;
			}
			else
			{
				this.requests.put(tileEntity, new ElectricityPack(amperes, voltage));
			}
		}
	}

	/**
	 * Sets this tile entity to stop producing energy in this network.
	 */
	public void stopRequesting(TileEntity tileEntity)
	{
		this.requests.remove(tileEntity);
	}
	
	/**
	 * @return The electricity produced in this electricity network
	 */
	public ElectricityPack getProduced()
	{
		ElectricityPack totalElectricity = new ElectricityPack(0, 0);

		Iterator it = this.currentlyProducing.entrySet().iterator();
		System.out.println(this.currentlyProducing.size());
		while (it.hasNext())
		{
			Map.Entry pairs = (Map.Entry) it.next();

			if (pairs != null)
			{
				if (pairs.getKey() == null)
				{
					it.remove();
					continue;
				}

				if (((TileEntity) pairs.getKey()).isInvalid())
				{
					it.remove();
					continue;
				}
				System.out.println(((TileEntity) pairs.getKey()).isInvalid() + "vs" + this.currentlyProducing.size());

				ElectricityPack pack = (ElectricityPack) pairs.getValue();

				if (pairs.getKey() != null && pairs.getValue() != null)
				{
					totalElectricity.amperes += pack.amperes;
					totalElectricity.voltage = Math.max(totalElectricity.voltage, pack.voltage);
				}
			}
		}

		return totalElectricity;
	}

	/**
	 * @return How much electricity this network needs.
	 */
	public ElectricityPack getRequest()
	{
		ElectricityPack totalElectricity = new ElectricityPack(0, 0);

		Iterator it = this.requests.entrySet().iterator();

		while (it.hasNext())
		{
			Map.Entry pairs = (Map.Entry) it.next();

			if (pairs != null)
			{
				if (pairs.getKey() == null)
				{
					it.remove();
					continue;
				}

				if (((TileEntity) pairs.getKey()).isInvalid())
				{
					it.remove();
					continue;
				}

				ElectricityPack pack = (ElectricityPack) pairs.getValue();

				if (pairs.getKey() != null && pairs.getValue() != null)
				{
					totalElectricity.amperes += pack.amperes;
					totalElectricity.voltage = Math.max(totalElectricity.voltage, pack.voltage);
				}
			}
		}

		return totalElectricity;
	}

	/**
	 * @param tileEntity
	 * @return The electricity being input into this tile entity.
	 */
	public ElectricityPack requestElectricity(TileEntity tileEntity)
	{
		ElectricityPack totalElectricity = new ElectricityPack(0, 0);

		if (this.requests.containsKey(tileEntity))
		{
			// Calculate the electricity this tile entity is receiving in percentage.
			totalElectricity = this.getProduced();

			if (totalElectricity.getWatts() > 0)
			{
				ElectricityPack totalRequest = this.getRequest();
				ElectricityPack tileRequest = this.requests.get(tileEntity);
				totalElectricity.amperes *= tileRequest.amperes / totalRequest.amperes;

				return totalElectricity;
			}
		}

		return totalElectricity;
	}

	public void addConductor(IConductor newConductor)
	{
		this.cleanConductors();

		if (!conductors.contains(newConductor))
		{
			conductors.add(newConductor);
			newConductor.setNetwork(this);
		}
	}

	/**
	 * Get only the electric units that can receive electricity from the given side.
	 */
	public List<TileEntity> getReceivers()
	{
		List<TileEntity> receivers = new ArrayList<TileEntity>();

		Iterator it = this.requests.entrySet().iterator();

		while (it.hasNext())
		{
			Map.Entry pairs = (Map.Entry) it.next();

			if (pairs != null)
			{
				receivers.add((TileEntity) pairs.getKey());
			}
		}

		return receivers;
	}

	public void cleanConductors()
	{
		for (int i = 0; i < conductors.size(); i++)
		{
			if (conductors.get(i) == null)
			{
				conductors.remove(i);
			}
			else if (((TileEntity) conductors.get(i)).isInvalid())
			{
				conductors.remove(i);
			}
		}
	}

	public void setNetwork()
	{
		this.cleanConductors();

		for (IConductor conductor : this.conductors)
		{
			conductor.setNetwork(this);
		}
	}

	public void onOverCharge()
	{
		this.cleanConductors();

		for (int i = 0; i < conductors.size(); i++)
		{
			conductors.get(i).onOverCharge();
		}
	}

	public double getLowestAmpTolerance()
	{
		double lowestAmp = 0;

		for (IConductor conductor : conductors)
		{
			if (lowestAmp == 0 || conductor.getMaxAmps() < lowestAmp)
			{
				lowestAmp = conductor.getMaxAmps();
			}
		}

		return lowestAmp;
	}

	/**
	 * This function is called to refresh all conductors in this network
	 */
	public void refreshConductors()
	{
		for (int j = 0; j < this.conductors.size(); j++)
		{
			IConductor conductor = this.conductors.get(j);
			conductor.refreshConnectedBlocks();
		}
	}
}
