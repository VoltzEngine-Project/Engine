package resonant.lib.prefab.fluid

import net.minecraftforge.fluids.FluidTank

/**
 * Prefab interface to create a few common getTank methods for most tank handlers
 * Created by robert(Darkguardsman) on 9/25/2014.
 */
trait TTankProvider {

  /** Gets the primary tank for this object */
  abstract def getPrimaryTank: FluidTank

  /** Gets the tank for the given fluid.
   *
   * Default implementation calls getPrimaryTank. Its suggested to override
   * this if you have several tank or restrict fluids to set tanks
   *
   * @param fluid - fluid instance, should be registered but not required
    *
   * @return instance of the fluid tank or null if there is no tank/match
   */
  def getTankForFluid(fluid: FluidTank): FluidTank =
  {
    return getPrimaryTank
  }
}