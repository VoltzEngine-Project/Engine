package com.builtbroken.lib.debug

import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent

/**
 * A handler that allows GUI display on the F3 GUI
 * @author Calclavia
 */
object F3Handler
{
  @SubscribeEvent
  def onRenderTick(event: RenderTickEvent)
  {
    val minecraft = FMLClientHandler.instance().getClient

    if (minecraft.currentScreen == null && minecraft.gameSettings.showDebugInfo)
    {

    }
  }

}
