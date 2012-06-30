package net.minecraft.src.universalelectricity;

import java.util.ArrayList;
import java.util.List;

/**
 * Managers an electric connection between a wire and consumers and producers
 * @author Calclavia
 *
 */
public class ElectricityConnection
{
	private static List<UETileEntityConductor> conductors = new ArrayList<UETileEntityConductor>();
	private static List<UEIConsumer> consumers = new ArrayList<UEIConsumer>();
	private static List<UEIProducer> producers = new ArrayList<UEIProducer>();
	 
	public void addConsumer(UEIConsumer consumer)
	{
		consumers.add(consumer);
	}
	  
	public void addProducer(UEIProducer producer)
	{
		producers.add(producer);
	}	
}
