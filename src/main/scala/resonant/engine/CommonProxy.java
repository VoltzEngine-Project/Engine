package resonant.engine;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayer;
import resonant.lib.grid.UpdateTicker;
import resonant.lib.prefab.AbstractProxy;
import resonant.lib.grid.UpdateTicker$;

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

    @Override
    public void init()
    {
        if (!UpdateTicker.useThreads())
            FMLCommonHandler.instance().bus().register(UpdateTicker$.MODULE$);

        if(Loader.isModLoaded("UniversalElectricity"))
        {
            throw new RuntimeException("UniversalElectricity is already contained within Resonant Engine and shouldn't be installed as a standalone");
        }
    }
}
