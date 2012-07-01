package net.minecraft.src.universalelectricity.electricity;

import java.util.ArrayList;
import java.util.List;

public class ElectricityManager
{
	private static List<ElectricityConnection> connections = new ArrayList<ElectricityConnection>();
	  
	public static void onUpdate()
	{
		//Send electricity from all connected producers to consumers
	}         
}