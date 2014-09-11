package universalelectricity.api.core.grid.sim;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Applied to any object that provides a pressure to control movement of input.
 * Pressure is not just applied to fluids as it can represent voltage, and force in general.
 * @author Darkguardsman
 */
public interface IPressure
{
    /**
     * Gets the pressure on the given side.
     *
     * @param type - simulator type for checking some internal connection logic
     * @param side - side of the machine
     *
     * @return value for pressure,
     * neg will say the side is an input,
     * pos will say the side is an output,
     * zero will say the side has no effect on default pressure, but will still receive pressure if connections exist
     */
    public double getPressure(SimType type, ForgeDirection side);
}
