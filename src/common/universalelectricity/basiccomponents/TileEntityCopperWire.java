package universalelectricity.basiccomponents;

import universalelectricity.prefab.TileEntityConductor;

public class TileEntityCopperWire extends TileEntityConductor
{
    @Override
    public double getResistance()
    {
        return 0.0000000168;
    }

	@Override
	public double getMaxVoltage()
	{
		return 500;
	}
}
