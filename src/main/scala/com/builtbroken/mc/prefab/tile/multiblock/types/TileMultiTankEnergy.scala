package com.builtbroken.mc.prefab.tile.multiblock.types

import com.builtbroken.mc.api.tile.ITankProvider
import com.builtbroken.mc.prefab.tile.traits.TFluidHandler
import net.minecraftforge.fluids.{Fluid, IFluidTank}

/**
  * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
  *      Created by Dark(DarkGuardsman, Robert) on 2/17/2017.
  */
class TileMultiTankEnergy extends TileMultiEnergy with TFluidHandler {
  override def getTank(fluid: Fluid): IFluidTank = {
    if (getHost != null && getHost.isInstanceOf[ITankProvider]) {
      return getHost.asInstanceOf[ITankProvider].getTankForFluid(fluid)
    }
    return null
  }
}
