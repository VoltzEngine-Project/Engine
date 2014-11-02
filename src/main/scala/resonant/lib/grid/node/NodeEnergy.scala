package resonant.lib.grid.node

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.ISave
import resonant.lib.grid.Compatibility
import resonant.api.electric.{EnergyStorage, IEnergyNode}
import resonant.api.grid.{IUpdate, INodeProvider}
import java.util.Map

/**
 * Any node capable of handling energy
 *
 * @author Calclavia
 */
abstract class NodeEnergy[C](parent:INodeProvider) extends NodeConnector[C](parent)
{
  def energy : Double
  def power : Double
}