package calclavia.lib.thermal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import calclavia.api.mffs.fortron.FrequencyGrid;
import calclavia.api.mffs.fortron.IServerThread;
import calclavia.components.CalclaviaLoader;
import net.minecraft.server.ServerListenThread;
import net.minecraft.server.ThreadMinecraftServer;
import net.minecraftforge.common.ForgeDirection;
import scala.annotation.meta.getter;
import universalelectricity.api.net.IUpdate;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

/**
 * A grid managing the flow of thermal energy.
 * 
 * TODO: Make this not temperature based, but more "energy based".
 */
public class ThermalGrid implements IUpdate
{
	public static final ThermalGrid CLIENT_INSTANCE = new ThermalGrid();
	public static final ThermalGrid SERVER_INSTANCE = new ThermalGrid();

	private final float spread = 0.01f;
	private final HashMap<VectorWorld, Float> thermalSource = new HashMap<VectorWorld, Float>();

	public float getDefaultTemperature(VectorWorld position)
	{
		return ThermalPhysics.getTemperatureForCoordinate(position.world, position.intX(), position.intZ());
	}

	public void addTemperature(VectorWorld position, float deltaTemperature)
	{
		synchronized (thermalSource)
		{
			float original;
			float defaultTemperature = getDefaultTemperature(position);

			if (thermalSource.containsKey(position))
				original = thermalSource.get(position);
			else
				original = defaultTemperature;

			float newTemperature = original + deltaTemperature;

			if (Math.abs(newTemperature - defaultTemperature) > 0.1)
				thermalSource.put(position, original + deltaTemperature);
			else
				thermalSource.remove(position);
		}
	}

	public float getTemperature(VectorWorld position)
	{
		synchronized (thermalSource)
		{
			if (thermalSource.containsKey(position))
				return thermalSource.get(position);
			else
				return ThermalPhysics.getTemperatureForCoordinate(position.world, position.intX(), position.intZ());
		}
	}

	@Override
	public void update()
	{
		Iterator<Entry<VectorWorld, Float>> it = new HashMap<VectorWorld, Float>(thermalSource).entrySet().iterator();
		//System.out.println(thermalSource.size());
		final float loss = 0.01f;

		while (it.hasNext())
		{
			Entry<VectorWorld, Float> entry = it.next();

			// Distribute temperature
			VectorWorld pos = entry.getKey();

			// Try to restore to equilibium
			float currentTemperature = getTemperature(pos);
			// addTemperature(pos, (getDefaultTemperature(pos) - currentTemperature) * spread );

			if (currentTemperature < 0)
			{
				thermalSource.remove(pos);
				continue;
			}

			/**
			 * Deal with different block types
			 */

			final float spread = Math.abs(Math.min((getDefaultTemperature(pos) - currentTemperature) * 0.001f, 0.01f));

			if (spread > 0)
			{
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
				{
					VectorWorld adjacent = (VectorWorld) pos.clone().translate(dir);
					float deltaTemperature = getTemperature(pos) - getTemperature(adjacent);

					if (deltaTemperature >= spread)
					{
						// System.out.println(deltaTemperature);
						addTemperature(adjacent, Math.min(deltaTemperature, spread));
						addTemperature(pos, -Math.min(deltaTemperature, spread));
					}
				}
			}
		}
	}

	@Override
	public boolean canUpdate()
	{
		return !CalclaviaLoader.proxy.isPaused();
	}

	@Override
	public boolean continueUpdate()
	{
		return true;
	}

	public static ThermalGrid instance()
	{
		Thread thr = Thread.currentThread();

		if ((thr instanceof ThreadMinecraftServer) || (thr instanceof ServerListenThread) || (thr instanceof IServerThread))
		{
			return SERVER_INSTANCE;
		}

		return CLIENT_INSTANCE;
	}
}
