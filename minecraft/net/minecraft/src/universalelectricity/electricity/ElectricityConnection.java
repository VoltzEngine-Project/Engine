package net.minecraft.src.universalelectricity.electricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraft.src.universalelectricity.extend.TileEntityConductor;

public class ElectricityConnection
{
	public int ID;
	public List<TileEntityConductor> conductors = new ArrayList<TileEntityConductor>();
	
	public ElectricityConnection(int ID, TileEntityConductor conductor)
	{
		this.ID = ID;
		this.conductors.add(conductor);
	}
	
	public void addConductor(TileEntityConductor newConductor)
	{
		this.cleanUpArray();
		
		if(!conductors.contains(newConductor))
		{
			conductors.add(newConductor);
		}
	}
	
	public List<IElectricUnit> getConnectedElectricUnits()
	{
		this.cleanUpArray();
		
		List<IElectricUnit> returnArray = new ArrayList<IElectricUnit>();
		
		for(TileEntityConductor conductor : conductors)
		{
			for(TileEntity tileEntity : conductor.connectedBlocks)
			{
				if(tileEntity != null)
				{
					if(tileEntity instanceof IElectricUnit)
					{
						if(!returnArray.contains((IElectricUnit)tileEntity))
						{
							returnArray.add((IElectricUnit)tileEntity);
						}
					}
				}
			}
		}
		
		return returnArray;
	}
	
	public void cleanUpArray()
	{
		for(int i = 0; i < conductors.size(); i++)
		{
			if(conductors.get(i) == null)
			{
				conductors.remove(i);
			}
			else if(conductors.get(i).isInvalid())
			{
				conductors.remove(i);
			}
		}
	}
}
