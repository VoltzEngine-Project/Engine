package calclavia.lib.thermal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.server.ServerListenThread;
import net.minecraft.server.ThreadMinecraftServer;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.api.net.IUpdate;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import calclavia.api.mffs.fortron.IServerThread;
import calclavia.components.CalclaviaLoader;
import calclavia.lib.thermal.EventThermal.EventThermalUpdate;

/**
 * A grid managing the flow of thermal energy.
 * 
 * TODO: Make this not temperature based, but more "energy based".
 */
public class ThermalGrid implements IUpdate
{
	public static final ThermalGrid CLIENT_INSTANCE = new ThermalGrid();
	public static final ThermalGrid SERVER_INSTANCE = new ThermalGrid();

	private final float spread = 1 / 7f;
	private final float loss = 0.1f;
	private final HashMap<VectorWorld, Float> thermalSource = new HashMap<VectorWorld, Float>();

	private int tick = 0;
	private final float deltaTime = 1 / 20f;

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

			if (Math.abs(newTemperature - defaultTemperature) > 0.4)
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
		// System.out.println("NODES " + thermalSource.size());

		while (it.hasNext())
		{
			Entry<VectorWorld, Float> entry = it.next();

			// Distribute temperature
			VectorWorld pos = entry.getKey();

			/**
			 * Deal with different block types
			 */
			float currentTemperature = getTemperature(pos);

			if (currentTemperature < 0)
			{
				thermalSource.remove(pos);
				continue;
			}

			float deltaFromEquilibrium = getDefaultTemperature(pos) - currentTemperature;

			EventThermalUpdate evt = new EventThermalUpdate(pos, currentTemperature, deltaFromEquilibrium, deltaTime);
			MinecraftForge.EVENT_BUS.post(evt);

			float loss = evt.heatLoss;
			addTemperature(pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(loss)));

			/**
			 * Spread heat to surrounding.
			 */
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				VectorWorld adjacent = (VectorWorld) pos.clone().translate(dir);
				float deltaTemperature = getTemperature(pos) - getTemperature(adjacent);

				Material adjacentMat = adjacent.world.getBlockMaterial(adjacent.intX(), adjacent.intY(), adjacent.intZ());

				float spread = (adjacentMat.isSolid() ? this.spread : this.spread / 2) * deltaTime;

				if (deltaTemperature > 0)
				{
					addTemperature(adjacent, deltaTemperature * spread);
					addTemperature(pos, -deltaTemperature * spread);
				}
			}

		}
	}

	@Override
	public boolean canUpdate()
	{
		return !CalclaviaLoader.proxy.isPaused();
		// && ++tick % 20 == 0;
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
