package resonant.lib.grid.thermal;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import resonant.lib.transform.vector.Vector3;
import resonant.lib.transform.vector.VectorWorld;

@Event.HasResult
public class BoilEvent extends WorldEvent
{
	public final FluidStack fluid;
	public final VectorWorld position;
	public final int maxSpread;
	public final boolean isReactor;

    @Deprecated /** Use the VectorWorld version instead */
    public BoilEvent(World world, Vector3 position, FluidStack source, FluidStack result, int maxSpread)
    {
        this(world, new VectorWorld(world, position), source, result, maxSpread, false);
    }

    @Deprecated /** Use the VectorWorld version instead */
    public BoilEvent(World world, VectorWorld position, FluidStack source, FluidStack result, int maxSpread, boolean isReactor)
    {
        this(new VectorWorld(world, position), source, result, maxSpread, isReactor);
    }

    /** @param position - location of the event in the world
     *  @param source - original fluid
     *  @param result - result of boiling the fluid
     *  @param maxSpread - max distance the result can spread
     */
    public BoilEvent(VectorWorld position, FluidStack source, FluidStack result, int maxSpread)
    {
        this(position, source, result, maxSpread, false);
    }

    /** @param position - location of the event in the world
     *  @param source - original fluid
     *  @param result - result of boiling the fluid
     *  @param maxSpread - max distance the result can spread
     *  @param isReactor - is the source of the event a reactor, this will prevent it from eating source blocks causing lag
     */
	public BoilEvent(VectorWorld position, FluidStack source, FluidStack result, int maxSpread, boolean isReactor)
	{
		super(position.world());
		this.position = position;
		this.fluid = result;
		this.maxSpread = maxSpread;
		this.isReactor = isReactor;
	}

	/**
	 * Fluid spread causes loss. Gets the remaining amount of fluid left after spreading.
	 */
	public FluidStack getRemainForSpread(int spread)
	{
		float spreadPercentage = (float) spread / (float) maxSpread;
		FluidStack returnFluid = fluid.copy();
		returnFluid.amount *= spreadPercentage;
		return returnFluid;
	}
}
