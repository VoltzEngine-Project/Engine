package resonant.api;

import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

public interface IReactor extends IFluidHandler
{
    public void heat(long energy);

    public float getTemperature();

    public boolean isOverToxic();

    public World world();
}
