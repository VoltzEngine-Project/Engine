package universalelectricity.core

import universalelectricity.core.grid.UpdateTicker
import cpw.mods.fml.common.FMLCommonHandler

class CommonProxy
{
  def init
  {
    if (!UpdateTicker.useThreads)
      FMLCommonHandler.instance().bus().register(UpdateTicker)
  }
}