package resonant.lib.thermal;

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
    public final boolean isReactor;

    public BoilEvent(World world, Vector3 position, FluidStack source, FluidStack result, int maxSpread)
    {
        this(world, position, source, result, maxSpread, false);
    }

    /** @param world - The World Objecto
     * @param position - The position in which the boiling happens.
     * @param result - The fluid being boiled.
     * @param maxSpread - The maximum distance the evaporated fluid can spread.
     * @param isReactor - Determined if heat source if from power generation or a weapon. */
    public BoilEvent(World world, Vector3 position, FluidStack source, FluidStack result, int maxSpread, boolean isReactor)
    {
        super(world);
        this.position = position;
        this.fluid = result;
        this.maxSpread = maxSpread;
        this.isReactor = isReactor;
    }

    /** Fluid spread causes loss. Gets the remaining amount of fluid left after spreading. */
    public FluidStack getRemainForSpread(int spread)
    {
        float spreadPercentage = (float) spread / (float) maxSpread;
        FluidStack returnFluid = fluid.copy();
        returnFluid.amount *= spreadPercentage;
        return returnFluid;
    }
}
