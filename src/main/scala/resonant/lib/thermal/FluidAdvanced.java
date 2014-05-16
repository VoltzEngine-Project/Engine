package resonant.lib.thermal;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/** An advanced fluid that takes account in temperature.
 * 
 * @author Calclavia */
public class FluidAdvanced extends Fluid
{
    public FluidAdvanced(String fluidName)
    {
        super(fluidName);
    }

    @Override
    public int getTemperature(FluidStack stack)
    {
        if (stack.tag.hasKey("Temperature"))
        {
            return stack.tag.getInteger("Temperature");
        }

        return getTemperature();
    }

    public void setTemperature(FluidStack stack, int temperature)
    {
        stack.tag.setInteger("Temperature", temperature);
    }
}
