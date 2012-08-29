package universalelectricity.basiccomponents;

import universalelectricity.extend.TileEntityConductor;

public class TileEntityCopperWire extends TileEntityConductor
{
    @Override
    public float getResistance()
    {
        return 0.3f;
    }

	@Override
	public float getVoltage()
	{
		return 500;
	}
}
