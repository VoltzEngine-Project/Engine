package resonant.api.mffs.fortron;

import net.minecraft.world.World;
import resonant.api.blocks.IBlockFrequency;
import resonant.lib.transform.region.Cuboid;
import resonant.lib.transform.vector.Vector3;

import java.util.Set;

/**
 * A grid MFFS uses to search for machines with frequencies that can be linked and spread Fortron
 * energy.
 *
 * @author Calclavia
 */
public class FrequencyGridRegistry
{
	public static IFrequencyGrid CLIENT_INSTANCE;
	public static IFrequencyGrid SERVER_INSTANCE;

	public static IFrequencyGrid instance()
	{
		Thread thr = Thread.currentThread();

		if (thr.getName().equals("Server thread") || thr instanceof IServerThread)
		{
			return SERVER_INSTANCE;
		}

		return CLIENT_INSTANCE;
	}

	public static interface IFrequencyGrid
	{
		void add(IBlockFrequency tileEntity);

		void remove(IBlockFrequency tileEntity);

		Set<IBlockFrequency> getNodes();

		<C extends IBlockFrequency> Set<C> getNodes(Class<C> clazz);

		/**
		 * Gets a list of TileEntities that has a specific frequency.
		 */
		Set<IBlockFrequency> getNodes(int frequency);

		<C extends IBlockFrequency> Set<C> getNodes(Class<C> clazz, int frequency);

		/**
		 * Gets a list of TileEntities that has a specific frequency, within a radius around a position.
		 */
		Set<IBlockFrequency> getNodes(World world, Vector3 position, int radius, int frequency);

		<C extends IBlockFrequency> Set<C> getNodes(Class<C> clazz, World world, Vector3 position, int radius, int frequency);

		Set<IBlockFrequency> getNodes(World world, Cuboid region, int frequency);

		<C extends IBlockFrequency> Set<C> getNodes(Class<C> clazz, World world, Cuboid region, int frequency);
	}
}
