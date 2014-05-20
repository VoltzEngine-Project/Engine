package universalelectricity.core;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		super.init();
		MinecraftForge.EVENT_BUS.register(new CapeEventHandler());
	}
}
