package universalelectricity.core.grid.node

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.EnergyStorage
import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.api.core.grid.ISave
import universalelectricity.api.core.grid.IUpdate
import universalelectricity.api.core.grid.electric.IEnergyNode
import universalelectricity.compatibility.Compatibility
import java.util.Map

/**
 * Any node capable of handling energy
 *
 * @author Calclavia
 */
abstract class NodeEnergy(parent:INodeProvider) extends NodeConnector(parent)
{
  def energy : Double
  def power : Double
}