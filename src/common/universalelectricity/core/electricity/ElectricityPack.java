package universalelectricity.core.electricity;

import net.minecraft.src.TileEntity;

public class ElectricityPack
{
	public double amperes;
	public double voltage;

	public ElectricityPack(double amperes, double voltage)
	{
		this.amperes = amperes;
		this.voltage = voltage;
	}
}
