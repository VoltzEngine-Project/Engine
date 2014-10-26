package universalelectricity.core

import cpw.mods.fml.common.FMLCommonHandler
import universalelectricity.core.grid.UpdateTicker

class CommonProxy
{
  def init
  {
    if (!UpdateTicker.useThreads)
      FMLCommonHandler.instance().bus().register(UpdateTicker)
  }
}