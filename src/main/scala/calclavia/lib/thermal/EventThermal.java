package calclavia.lib.thermal;

import net.minecraftforge.event.Event;
import universalelectricity.api.vector.VectorWorld;

public abstract class EventThermal extends Event
{
	public final VectorWorld position;
	public final float temperature;
	public final float deltaTemperature;
	public final float deltaTime;
	public float heatLoss = 0.1f;

	public EventThermal(VectorWorld position, float temperature, float deltaTemperature, float deltaTime)
	{
		this.position = position;
		this.temperature = temperature;
		this.deltaTemperature = deltaTemperature;
		this.deltaTime = deltaTime;
	}

	public static class EventThermalUpdate extends EventThermal
	{
		public EventThermalUpdate(VectorWorld position, float temperature, float deltaTemperature, float deltaTime)
		{
			super(position, temperature, deltaTemperature, deltaTime);
		}
	}
}
