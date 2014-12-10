package resonant.engine.content.debug

import java.util

import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._
import resonant.api.tile.node.INode
import resonant.lib.content.prefab.java.TileIO

/**
 * Designed to debug fluid devices by draining everything that comes in at one time
 *
 * @author DarkGuardsman
 */
class TileInfiniteFluid extends TileIO(Material.iron) with IFluidHandler
{
  var tank: FluidTank = new FluidTank(Integer.MAX_VALUE)
  var active: Boolean = false

  saveIOMap = true
  ioMap = 728

  override def update()
  {
    super.update

    if (active)
    {
      ForgeDirection.VALID_DIRECTIONS.filter(getOutputDirections.contains(_)).foreach(
        direction =>
        {
          val tile: TileEntity = (toVectorWorld + direction).getTileEntity

          if (tile.isInstanceOf[IFluidHandler])
          {
            (tile.asInstanceOf[IFluidHandler]).fill(direction.getOpposite, tank.getFluid, true)
          }

        });
    }
  }

  override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] =
  {
    return Array[FluidTankInfo](this.tank.getInfo)
  }

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int =
  {
    if (getInputDirections.contains(from))
    {
      return resource.amount
    }
    return 0
  }

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack =
  {
    if (getOutputDirections.contains(from))
    {
      return resource
    }
    return null
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    if (getOutputDirections.contains(from))
    {
      return this.tank.drain(maxDrain, false)
    }
    return null
  }

  override def canFill(from: ForgeDirection, fluid: Fluid): Boolean = getInputDirections.contains(from)

  override def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = getOutputDirections.contains(from)

  /*
    def onActivated(entityPlayer: EntityPlayer): Boolean =
    {
      if (entityPlayer != null && entityPlayer.getHeldItem != null)
      {
        if (entityPlayer.getHeldItem.getItem eq Item.stick)
        {
          this.active = !this.active
          entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("[FluidVoid] Pumping:" + this.active))
          return true
        }
        var stack: FluidStack = FluidContainerRegistry.getFluidForFilledItem(entityPlayer.getHeldItem)
        if (stack != null)
        {
          stack = stack.copy
          stack.amount = Integer.MAX_VALUE
          this.tank.setFluid(stack)
          entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("[FluidVoid] Fluid:" + stack.getFluid.getName))
          return true
        }
      }
      return false
    }*/
  override def getNodes(nodes: util.List[INode]): Unit =
  {

  }

  override def getNode[N <: INode](nodeType: Class[_ <: N], from: ForgeDirection): N = null.asInstanceOf[N];
}