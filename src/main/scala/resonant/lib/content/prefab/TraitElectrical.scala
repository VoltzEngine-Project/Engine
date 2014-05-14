package resonant.lib.content.prefab

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.ForgeDirection
import resonant.lib.utility.nbt.ISaveObj
import universalelectricity.api.CompatibilityModule
import universalelectricity.api.UniversalClass
import universalelectricity.api.energy.EnergyStorageHandler
import universalelectricity.api.energy.IEnergyContainer
import universalelectricity.api.energy.IEnergyInterface
import universalelectricity.api.vector.Vector3

@UniversalClass //@Interface()
trait TraitElectrical extends TraitIO with IEnergyInterface with IEnergyContainer with ISaveObj {
  protected var energy: EnergyStorageHandler = _

  //@Callback
  protected def recharge(stack: ItemStack) {
    if (this.energy != null) {
      this.energy.extractEnergy(CompatibilityModule.chargeItem(stack, this.energy.getEnergy, true), true)
    }

  }

  //@Callback
  protected def discharge(stack: ItemStack) {
    if (this.energy != null) {
      this.energy.receiveEnergy(CompatibilityModule.dischargeItem(stack, this.energy.getEmptySpace, true), true)
    }

  }

  override def canConnect(dir: ForgeDirection, obj: Object): Boolean =
    {
      if (CompatibilityModule.isHandler(obj)) {
        if (dir == null || dir.equals(ForgeDirection.UNKNOWN)) {
          return false
        }
        return getInputDirections.contains(dir) || getOutputDirections.contains(dir)
      }
      return false
    }

  //TODO: Add OC Event firing
  override def onReceiveEnergy(from: ForgeDirection, receive: Long, doReceive: Boolean): Long =
    {
      if (this.energy != null && (from == ForgeDirection.UNKNOWN || this.getInputDirections.contains(from))) {
        return this.energy.receiveEnergy(receive, doReceive)
      }
      return 0
    }

  //TODO: Add OC Event firing
  override def onExtractEnergy(from: ForgeDirection, extract: Long, doExtract: Boolean): Long =
    {
      if (this.energy != null && (from == ForgeDirection.UNKNOWN || this.getOutputDirections.contains(from))) {
        return this.energy.extractEnergy(extract, doExtract)
      }
      return 0
    }

  protected def produce(outputEnergy: Long, coord: Vector3, world: World): Long =
    {
      var usedEnergy: Long = 0;

      for (dir: ForgeDirection <- this.getOutputDirections) {
        if (outputEnergy > 0) {
          val tile: TileEntity = coord.translate(dir).getTileEntity(world)

          if (tile != null) {
            usedEnergy += CompatibilityModule.receiveEnergy(tile, dir.getOpposite, outputEnergy, true)
          }
        }
      }
      return usedEnergy

    }

  protected def produce(coord: Vector3, world: World): Long =
    {
      return energy.extractEnergy(produce(energy.getMaxExtract, coord, world), true)

    }

  /**
   * Sets the amount of energy this unit stored.
   *
   * This function is NOT recommended for calling.
   */
  override def setEnergy(from: ForgeDirection, amount: Long) =
    {
      if (energy != null)
        energy.setEnergy(amount)
    }

  //@Callback
  override def getEnergy(from: ForgeDirection): Long =
    {
      if (energy != null) {
        return energy.getEnergy()
      }
      return 0
    }

  //@Callback
  override def getEnergyCapacity(from: ForgeDirection): Long =
    {
      if (energy != null) {
        return energy.getEnergyCapacity
      } else
        return 0

    }

  override def save(nbt: NBTTagCompound) {
    super.save(nbt);

    if (energy != null) {
      energy.readFromNBT(nbt)
    }
  }

  override def load(nbt: NBTTagCompound) {
    super.load(nbt);

    if (energy != null) {
      energy.writeToNBT(nbt)
    }
  }

}