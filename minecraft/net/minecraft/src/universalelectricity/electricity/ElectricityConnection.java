package net.minecraft.src.universalelectricity.electricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.universalelectricity.extend.TileEntityConductor;

/**
 * Managers an electric connection between a wire and consumers and producers
 * @author Calclavia
 *
 */
public class ElectricityConnection
{
	private static List<TileEntityConductor> conductors = new ArrayList<TileEntityConductor>();
	private static List<IElectricityConsumer> consumers = new ArrayList<IElectricityConsumer>();
	private static List<IElectricityProducer> producers = new ArrayList<IElectricityProducer>();
	 
	public void addConsumer(IElectricityConsumer consumer)
	{
		consumers.add(consumer);
	}
	  
	public void addProducer(IElectricityProducer producer)
	{
		producers.add(producer);
	}	
}
