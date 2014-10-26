package resonant.engine;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import resonant.lib.prefab.AbstractProxy;
import universalelectricity.core.grid.UpdateTicker;
import universalelectricity.core.grid.UpdateTicker$;

/**
 * The Resonant Engine common proxy
 *
 * @author Calclavia
 */
public class CommonProxy extends AbstractProxy
{
	public boolean isPaused()
	{
		return false;
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}

    public void init()
    {
        if (!UpdateTicker.useThreads())
            FMLCommonHandler.instance().bus().register(UpdateTicker$.MODULE$);
    }
}
