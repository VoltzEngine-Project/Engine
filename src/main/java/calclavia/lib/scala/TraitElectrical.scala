package calclavia.lib.scala

import universalelectricity.api.energy.{EnergyStorageHandler, IEnergyContainer, IEnergyInterface}
import net.minecraft.item.ItemStack
import universalelectricity.api.{CompatibilityModule, UniversalClass}
import net.minecraftforge.common.ForgeDirection
import calclavia.lib.utility.nbt.ISaveObj
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import universalelectricity.api.vector.Vector3
import net.minecraft.world.World

@UniversalClass
trait TraitElectrical extends TraitIO with IEnergyInterface with IEnergyContainer with ISaveObj
{
  protected var energy: EnergyStorageHandler

  def recharge (stack: ItemStack)
  {
    if (this.energy != null)
    {
      this.energy.extractEnergy(CompatibilityModule.chargeItem(stack, this.energy.getEnergy(), true), true)
    }

  }

  def discharge(stack: ItemStack)
  {
    if (this.energy != null)
    {
      this.energy.receiveEnergy(CompatibilityModule.dischargeItem(stack, this.energy.getEmptySpace, true), true)
    }

  }

  def canConnect(dir: ForgeDirection, obj: Object): Boolean =
  {
    if (CompatibilityModule.isHandler(obj))
    {
      if (dir == null || dir.equals(ForgeDirection.UNKNOWN))
      {
        return false
      }
      return this.getInputDirections.contains(direction) || this.getOutputDirections.contains(direction)
    }
    return false
  }

  def getEnergy(from: ForgeDirection): Long =
  {
    if (this.energy != null)
    {
      return this.energy.getEnergy()
    }
    return 0
  }

  def getEnergyCapacity(from: ForgeDirection): Long =
  {
    if (this.energy != null)
    {
      return this.energy.getEnergyCapacity
    }
    else
      return 0

  }

  def onReceiveEnergy(from: ForgeDirection, receive: Long, doReceive: Boolean)
  {
    if (this.energy != null && (from == ForgeDirection.UNKNOWN || this.getInputDirections.contains(from)))
    {
      return this.energy.receiveEnergy(receive, doReceive)
    }
    return 0
  }

  def onExtractEnergy(from: ForgeDirection, extract: Long, doExtract: Boolean)
  {
    if (this.energy != null && (from == ForgeDirection.UNKNOWN || this.getOutputDirections.contains(from)))
    {
      return this.energy.extractEnergy(extract, doExtract)
    }
    return 0
  }

  protected def produce(outputEnergy: Long, coord: Vector3, world: World): Long =
  {
    var usedEnergy: Long = 0;

    for (dir: ForgeDirection <- this.getOutputDirections)
    {
      if (outputEnergy > 0)
      {
        val tile: TileEntity = coord.translate(dir).getTileEntity(world)

        if (tile != null)
        {
          usedEnergy += CompatibilityModule.receiveEnergy(tile, dir.getOpposite, outputEnergy, true)
        }
      }
    }
    return usedEnergy

  }

  protected def produce(coord: Vector3, world: World): Long =
  {
    var totalUsed: Long = 0;

    for (dir: ForgeDirection <- this.getOutputDirections)
    {
      if (outputEnergy > 0)
      {
        val tile: TileEntity = coord.translate(dir).getTileEntity(world)

        if (tile != null)
        {
          val used: Long = CompatibilityModule.receiveEnergy(tile, dir.getOpposite, energy.extractEnergy(energy.getEnergy, false), true)
          totalUsed += this.energy.extractEnergy(used, true)
        }
      }
    }
    return totalUsed

  }

  override def save(nbt: NBTTagCompound)
  {
    if (this.energy != null)
    {
      this.energy.readFromNBT(nbt)
    }
  }

  override def load(nbt: NBTTagCompound)
  {
    if (this.energy != null)
    {
      this.energy.writeToNBT(nbt)
    }
  }



}