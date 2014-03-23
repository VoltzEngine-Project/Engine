package calclavia.lib.thermal;

import net.minecraft.world.World;
import net.minecraftforge.event.Event.HasResult;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.api.vector.Vector3;

@HasResult
public class BoilEvent extends WorldEvent
{
	public final FluidStack fluid;
	public final Vector3 position;
	public final int maxSpread;

	/**
	 * @param world - The World Objecto
	 * @param position - The position in which the boiling happens.
	 * @param result - The fluid being boiled.
	 * @param maxSpread - The maximum distance the evaporated fluid can spread.
	 */
	public BoilEvent(World world, Vector3 position, FluidStack source, FluidStack result, int maxSpread)
	{
		super(world);
		this.position = position;
		this.fluid = result;
		this.maxSpread = maxSpread;
	}

	/**
	 * Fluid spread causes loss. Gets the remaining amount of fluid left after spreading.
	 * 
	 */
	public FluidStack getRemainForSpread(int spread)
	{
		float spreadPercentage = (float) spread / (float) maxSpread;
		FluidStack returnFluid = fluid.copy();
		returnFluid.amount *= spreadPercentage;
		return returnFluid;
	}
}
