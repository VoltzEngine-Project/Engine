package basiccomponents;

import universalelectricity.extend.TileEntityConductor;


/**
 * The Class TileEntityCopperWire.
 */
public class TileEntityCopperWire extends TileEntityConductor
{
    
    /**
     * Instantiates a new tile entity copper wire.
     */
    public TileEntityCopperWire()
    {
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.TileEntityConductor#getResistance()
     */
    public double getResistance()
    {
        return 0.3D;
    }
}
