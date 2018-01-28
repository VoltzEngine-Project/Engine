package com.builtbroken.test.prefab.energy

import com.builtbroken.mc.framework.energy.data.EnergyBuffer
import net.minecraft.tileentity.TileEntity

/**
 * Created by Dark on 8/15/2015.
 */
class TileTEnergyHandler extends TileEntity{

  var buffer: EnergyBuffer = new EnergyBuffer(100)
}
