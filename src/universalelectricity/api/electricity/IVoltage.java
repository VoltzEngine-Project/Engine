package universalelectricity.api.electricity;

import net.minecraftforge.common.ForgeDirection;

/** Implement this on your TileEntity if it has a voltage based energy output. When using voltage the
 * network will look at your machine different. In some cases your machine may not output power if
 * the networks base voltage is higher then yours.
 * 
 * @author Calclavia */
public interface IVoltage
{

    /** Voltage emitted from the side */
    public long getVoltage(ForgeDirection direction);
}
