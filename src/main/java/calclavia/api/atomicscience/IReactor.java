package calclavia.api.atomicscience;

import net.minecraftforge.fluids.IFluidHandler;

public interface IReactor extends IFluidHandler
{
	public void heat(long energy);

	public boolean isOverToxic();
}
