package resonant.engine.grid.thermal;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.tile.IReactor;
import resonant.engine.ResonantEngine;
import resonant.engine.grid.thermal.EventThermal.EventThermalUpdate;
import resonant.api.IUpdate;
import resonant.lib.transform.vector.VectorWorld;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A grid managing the flow of thermal energy.
 * <p/>
 * TODO: Make this not temperature based, but more "energy based".
 */
public class ThermalGrid implements IUpdate
{
	private final static HashMap<VectorWorld, Float> thermalSource = new HashMap();
	private final float spread = 1 / 7f;
	private final float loss = 0.1f;
	private final float deltaTime = 1 / 20f;
	private int tick = 0;

	public static float getDefaultTemperature(VectorWorld position)
	{
		return ThermalPhysics.getTemperatureForCoordinate(position.world(), position.xi(), position.zi());
	}

	public static void addTemperature(VectorWorld position, float deltaTemperature)
	{
		float original;
		float defaultTemperature = getDefaultTemperature(position);

		if (thermalSource.containsKey(position))
		{
			original = thermalSource.get(position);
		}
		else
		{
			original = defaultTemperature;
		}

		float newTemperature = original + deltaTemperature;

		if (Math.abs(newTemperature - defaultTemperature) > 0.4)
		{
			thermalSource.put(position, original + deltaTemperature);
		}
		else
		{
			thermalSource.remove(position);
		}

	}

	public static float getTemperature(VectorWorld position)
	{
		if (thermalSource.containsKey(position))
		{
			return thermalSource.get(position);
		}
		else
		{
			return ThermalPhysics.getTemperatureForCoordinate(position.world(), position.xi(), position.zi());
		}
	}

	@Override
	public void update(double delta)
	{
		Iterator<Entry<VectorWorld, Float>> it = new HashMap<VectorWorld, Float>(thermalSource).entrySet().iterator();
		// System.out.println("NODES " + thermalSource.size());

		while (it.hasNext())
		{
			Entry<VectorWorld, Float> entry = it.next();

			// Distribute temperature
			VectorWorld pos = entry.getKey();

			/** Deal with different block types */
			float currentTemperature = getTemperature(pos);

			if (currentTemperature < 0)
			{
				thermalSource.remove(pos);
				continue;
			}

			float deltaFromEquilibrium = getDefaultTemperature(pos) - currentTemperature;

			// Determine if position of heat is IReactor based.
			TileEntity possibleReactor = pos.getTileEntity();

			EventThermalUpdate evt = new EventThermalUpdate(pos, currentTemperature, deltaFromEquilibrium, deltaTime, possibleReactor != null && possibleReactor instanceof IReactor);
			MinecraftForge.EVENT_BUS.post(evt);

			float loss = evt.heatLoss;
			addTemperature(pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(loss)));

			/** Spread heat to surrounding. */
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				VectorWorld adjacent = (VectorWorld) pos.clone().$plus(dir);
				float deltaTemperature = getTemperature(pos) - getTemperature(adjacent);

				Material adjacentMat = adjacent.world().getBlock(adjacent.xi(), adjacent.yi(), adjacent.zi()).getMaterial();

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
		return !ResonantEngine.proxy.isPaused();
		// && ++tick % 20 == 0;
	}

	@Override
	public boolean continueUpdate()
	{
		return true;
	}
}
