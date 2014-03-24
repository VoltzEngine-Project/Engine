package calclavia.api;

import net.minecraft.world.World;

/** Simple interface to define that an object has a position in the world
 * 
 * @author DarkGuardsman */
public interface IPos
{
    World world();
    
    double x();
    
    double y();
    
    double z();
}
