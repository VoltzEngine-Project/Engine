package com.builtbroken.mc.framework.multiblock.types

import com.builtbroken.mc.api.tile.provider.ITankProvider
import com.builtbroken.mc.framework.multiblock.TileMulti
import com.builtbroken.mc.prefab.tile.traits.TFluidHandler
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
