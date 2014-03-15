package calclavia.lib.thermal;

import net.minecraftforge.event.Event;
import universalelectricity.api.vector.VectorWorld;

public abstract class EventThermal extends Event
{
	public final VectorWorld position;
	public final float temperature;
	public float heatLoss = 0.2f;

	public EventThermal(VectorWorld position, float temperature)
	{
		this.position = position;
		this.temperature = temperature;
	}

	public static class EventThermalUpdate extends EventThermal
	{
		public EventThermalUpdate(VectorWorld position, float temperature)
		{
			super(position, temperature);
		}
	}
}
