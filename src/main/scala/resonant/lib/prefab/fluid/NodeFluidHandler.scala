package resonant.lib.prefab.fluid

import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTank}
import resonant.api.grid.INodeProvider
import resonant.lib.grid.node.NodeConnector

/** Version of NodeConnector designed for usage with fluids,
  * can be directly used as a tank object thanks to {@link TFluidTank}
  *
  * Created by robert(Darkguardsman) on 9/25/2014.
  *
  * @param parent - parent(TileEntity or Multipart) that contains this node
  */
class NodeFluidHandler(parent: INodeProvider) extends NodeConnector(parent) with TFluidHandler with TFluidTank
{
  /** Internal tank */
  private var tank: FluidTank = new FluidTank(1000);

  /** @param parent - parent(TileEntity or Multipart) that contains this node
    * @param milliBuckets - number of millibuckets to set the tank capacity to */
  def this(parent: INodeProvider, milliBuckets: Int)
  {
    this(parent)
    this.setCapacity(milliBuckets)
  }

  /** @param parent - parent(TileEntity or Multipart) that contains this node
    * @param tank - instance of FluidTank to set the internal tank to */
  def this(parent: INodeProvider, tank: FluidTank)
  {
    this(parent)
    this.tank = tank
  }

  override def getPrimaryTank: FluidTank = tank

  /** Sets the primary tank without checks
    *
    * @param t - tank, can be null but not recommended
    */
  def setPrimaryTank(t: FluidTank)
  {
    tank = t
  }

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int =
  {
    if (canConnect(from))
      return super.fill(from, resource, doFill)

    return 0
  }

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack =
  {
    if (canConnect(from))
      return super.drain(from, resource, doDrain)

    return null
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    if (canConnect(from))
      return super.drain(from, maxDrain, doDrain)

    return null
  }

  override def canFill(from: ForgeDirection, fluid: Fluid): Boolean = canConnect(from)

  override def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = canConnect(from)
}
