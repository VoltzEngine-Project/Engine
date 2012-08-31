package universalelectricity.electricity;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ElectricityTicker implements ITickHandler
{
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{	/*
		for(ElectricityManager instance : ElectricityManager.instances)
		{
			instance.tickStart(type, tickData);
		}*/
		
		if(ElectricityManager.instance != null)
		{
			ElectricityManager.instance.tickStart(type, tickData);
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		/*
		for(ElectricityManager instance : ElectricityManager.instances)
		{
			instance.tickEnd(type, tickData);
		}*/
		
		if(ElectricityManager.instance != null)
		{
			ElectricityManager.instance.tickEnd(type, tickData);
		}
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.SERVER, TickType.CLIENT);
	}

	@Override
	public String getLabel()
	{
		return "Electricity Manager";
	}
}
