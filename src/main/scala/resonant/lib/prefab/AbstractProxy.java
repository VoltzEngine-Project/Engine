package resonant.lib.prefab;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import resonant.lib.loadable.ILoadable;

/**
 * An abstract proxy that can be extended by any mod.
 */
public abstract class AbstractProxy implements IGuiHandler, ILoadable
{
	public void preInit()
	{

	}

	public void init()
	{

	}

	public void postInit()
	{

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
