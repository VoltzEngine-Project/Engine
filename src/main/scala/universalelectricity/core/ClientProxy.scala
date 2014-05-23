package universalelectricity.core

import _root_.net.minecraftforge.common.MinecraftForge

class ClientProxy extends CommonProxy
{
  override def init
  {
    super.init
    MinecraftForge.EVENT_BUS.register(new CapeEventHandler)
  }
}