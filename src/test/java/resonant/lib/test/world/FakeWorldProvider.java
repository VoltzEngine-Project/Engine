package resonant.lib.test.world;

import net.minecraft.world.WorldProvider;

/**
 * Created by robert on 11/20/2014.
 */
public class FakeWorldProvider extends WorldProvider
{
    @Override
    public String getDimensionName()
    {
        return "FakeWorld";
    }
}
