package resonant.lib.prefab;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;

public class ProxyBase implements IGuiHandler
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

	public boolean isPaused()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public EntityPlayer getPlayerFromNetHandler(INetHandler handler)
	{
		if (handler instanceof NetHandlerPlayServer)
		{
			return ((NetHandlerPlayServer) handler).playerEntity;
		}
		else
		{
			return null;
		}
	}

}
