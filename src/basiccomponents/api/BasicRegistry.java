package basiccomponents.api;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.common.Configuration;

/**
 * The main class for managing Basic Component items and blocks. Reference objects from this class
 * to add them to your recipes and such.
 * 
 * @author Calclavia
 */

public class BasicRegistry
{
	public static final Set<String> requests = new HashSet<String>();

	public static void register(String request)
	{
		requests.add(request);
	}

	/**
	 * Requests all items in Basic Components.
	 */
	public static void requestAll()
	{
		register("ingotCopper");
		register("ingotTin");

		register("oreCopper");
		register("oreTin");

		register("ingotSteel");
		register("dustSteel");
		register("plateSteel");

		register("ingotBronze");
		register("dustBronze");
		register("plateBronze");

		register("plateCopper");
		register("plateTin");
		register("plateIron");
		register("plateGold");

		register("circuitBasic");
		register("circuitAdvanced");
		register("circuitElite");

		register("motor");
		register("wrench");
	}
}
