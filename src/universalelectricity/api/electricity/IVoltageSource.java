package universalelectricity.api.electricity;

import net.minecraftforge.common.ForgeDirection;

/** Applied to electrical machines that are designed to act as sources of power in an electrical
 * network. Mainly used to calculate the over all voltage of a network correctly.
 * 
 * @author DarkGuardsman */
public interface IVoltageSource
{
    /** Can this machine emit voltage on the given side. Should
     * 
     * @param side - side that the voltage will be emitted on
     * @return true if at any given time this machine can output voltage on the side */
    public boolean canEmitVoltage(ForgeDirection side);
}
