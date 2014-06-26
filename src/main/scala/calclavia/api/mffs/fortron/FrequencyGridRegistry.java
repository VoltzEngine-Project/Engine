package calclavia.api.mffs.fortron;

import net.minecraft.world.World;
import resonant.api.blocks.IBlockFrequency;
import universalelectricity.core.transform.region.Cuboid;
import universalelectricity.core.transform.vector.Vector3;

import java.util.Set;

/**
 * A grid MFFS uses to search for machines with frequencies that can be linked and spread Fortron
 * energy.
 *
 * @author Calclavia
 */
public class FrequencyGridRegistry
{
	private static IFrequencyGrid CLIENT_INSTANCE = new resonant.engine.grid.frequency.FrequencyGrid();
	private static IFrequencyGrid SERVER_INSTANCE = new resonant.engine.grid.frequency.FrequencyGrid();

	public static IFrequencyGrid instance()
	{
		Thread thr = Thread.currentThread();

		if (thr.getName().equals("Server thread") || thr instanceof IServerThread)
		{
			return SERVER_INSTANCE;
		}

		return CLIENT_INSTANCE;
	}

	/**
	 * Called to re-initiate the grid. Used when server restarts or when player rejoins a world to
	 * clean up previously registered objects.
	 */
	public static void reinitiate()
	{
		CLIENT_INSTANCE = new resonant.engine.grid.frequency.FrequencyGrid();
		SERVER_INSTANCE = new resonant.engine.grid.frequency.FrequencyGrid();
	}

	public static interface IFrequencyGrid
	{
		void add(IBlockFrequency tileEntity);

		void remove(IBlockFrequency tileEntity);

		Set<IBlockFrequency> getNodes();

		Set<IBlockFrequency> getNodes(Class clazz);

		/**
		 * Gets a list of TileEntities that has a specific frequency.
		 */
		Set<IBlockFrequency> getNodes(int frequency);

		Set<IBlockFrequency> getNodes(Class clazz, int frequency);

		/**
		 * Gets a list of TileEntities that has a specific frequency, within a radius around a position.
		 */
		Set<IBlockFrequency> getNodes(World world, Vector3 position, int radius, int frequency);

		Set<IBlockFrequency> getNodes(Class clazz, World world, Vector3 position, int radius, int frequency);

		Set<IBlockFrequency> getNodes(World world, Cuboid region, int frequency);

		Set<IBlockFrequency> getNodes(Class clazz, World world, Cuboid region, int frequency);
	}
}
