package resonant.lib.prefab.block;

import net.minecraftforge.fluids.Fluid;

public class FluidColored extends Fluid
{
	private int color = 0xFFFFFF;

	public FluidColored(String fluidName)
	{
		super(fluidName);
	}

	@Override
	public int getColor()
	{
		return color;
	}

	public void setColor(int newColor)
	{
		color = newColor;
	}
}
