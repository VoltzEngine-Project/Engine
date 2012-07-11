package universalelectricity.electricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.TileEntity;
import universalelectricity.UniversalElectricity;
import universalelectricity.extend.TileEntityConductor;


/**
 * The Class ElectricityConnection.
 */
public class ElectricityConnection
{
	
	/** The id. */
	private int ID;
	
	/** The conductors. */
	public List<TileEntityConductor> conductors = new ArrayList<TileEntityConductor>();

	/**
	 * Instantiates a new electricity connection.
	 *
	 * @param ID the id
	 * @param conductor the conductor
	 */
	public ElectricityConnection(int ID, TileEntityConductor conductor)
	{
		this.ID = ID;
		this.addConductor(conductor);
	}

	/**
	 * Adds the conductor.
	 *
	 * @param newConductor the new conductor
	 */
	public void addConductor(TileEntityConductor newConductor)
	{
		this.cleanUpArray();

		if(!conductors.contains(newConductor))
		{
			conductors.add(newConductor);
			newConductor.connectionID = this.ID;
		}
	}

	/**
	 * Gets the connected electric units.
	 *
	 * @return the connected electric units
	 */
	public List<IElectricUnit> getConnectedElectricUnits()
	{
		this.cleanUpArray();

		List<IElectricUnit> returnArray = new ArrayList<IElectricUnit>();

		for(TileEntityConductor conductor : conductors)
		{
			for(byte i = 0; i < conductor.connectedBlocks.length; i++)
			{
				TileEntity tileEntity = conductor.connectedBlocks[i];

				if(tileEntity != null)
				{
					if(tileEntity instanceof IElectricUnit)
					{
						if(!returnArray.contains((IElectricUnit)tileEntity) && ((IElectricUnit)tileEntity).canReceiveFromSide(UniversalElectricity.getOrientationFromSide(i, (byte)2) ))
						{
							returnArray.add((IElectricUnit)tileEntity);
						}
					}
				}
			}
		}

		return returnArray;
	}

	/**
	 * Clean up array.
	 */
	public void cleanUpArray()
	{
		for(int i = 0; i < conductors.size(); i++)
		{
			if(conductors.get(i) == null)
			{
				conductors.remove(i);
			}
			else if(conductors.get(i).l())
			{
				conductors.remove(i);
			}
		}
	}

	/**
	 * Sets the id.
	 *
	 * @param ID the new id
	 */
	public void setID(int ID)
	{
		this.ID = ID;
		this.cleanUpArray();

		for(TileEntityConductor conductor : this.conductors)
		{
			conductor.connectionID = this.ID;
		}
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID()
	{
		return this.ID;
	}
}
