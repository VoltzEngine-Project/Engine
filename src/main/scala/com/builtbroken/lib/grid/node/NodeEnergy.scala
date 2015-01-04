package com.builtbroken.lib.grid.node

import com.builtbroken.api.tile.INodeProvider
import com.builtbroken.lib.grid.energy.EnergyStorage
import com.builtbroken.lib.prefab.TEnergyBuffer

/** Node which acts like glorified battery for storing energy.
 */
abstract class NodeEnergy[A <: AnyRef](parent:INodeProvider) extends NodeConnector[A](parent) with TEnergyBuffer
{
  var buffer : EnergyStorage = new EnergyStorage();

  /** Current energy value of the node
   * defaults to buffer.getEnergy()
    * @deprecated use getEnergy() so we fit with standards
   * @return energy as a double
   */
  @Deprecated
  def energy : Double = getEnergyStorage.getEnergy

  /** Current Watts value of the node
   * defaults to buffer.getEnergy()
    * @deprecated use getEnergy() so we fit with standards
   * @return energy as a double
   */
  @Deprecated
  def power : Double = getEnergyStorage.getEnergy

  override def getEnergyStorage(): EnergyStorage = buffer

}