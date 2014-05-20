package universalelectricity.core;

import universalelectricity.core.net.NetworkTickHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(NetworkTickHandler.INSTANCE);
	}
}
