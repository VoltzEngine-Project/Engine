package resonant.lib.prefab;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import resonant.lib.modproxy.ICompatProxy;

/**
 * An abstract proxy that can be extended by any mod.
 */
public abstract class AbstractProxy implements IGuiHandler, ICompatProxy
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
	public String modId()
	{
		return null;
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
