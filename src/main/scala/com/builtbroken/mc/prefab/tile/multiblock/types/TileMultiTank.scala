package com.builtbroken.mc.prefab.tile.multiblock.types

import com.builtbroken.mc.api.tile.ITankProvider
import com.builtbroken.mc.prefab.tile.traits.TFluidHandler
import com.builtbroken.mc.prefab.tile.multiblock.TileMulti
import net.minecraftforge.fluids.{IFluidTank, FluidTank, Fluid}

/**
 * Created by Dark on 8/9/2015.
 */
class TileMultiTank extends TileMulti with TFluidHandler {

  override def getTank(fluid: Fluid): IFluidTank = {
    if (getHost != null && getHost.isInstanceOf[ITankProvider]) {
      return getHost.asInstanceOf[ITankProvider].getTankForFluid(fluid)
    }
    return null
  }
}
