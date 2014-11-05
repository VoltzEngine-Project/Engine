package resonant.lib.grid.electric

import resonant.lib.grid.GridTicking

/**
 * @author Calclavia
 */
class DCGrid extends GridTicking[DCNode](classOf[DCNode])
{
  

  override def update(deltaTime: Double)
  {

  }

  override def canUpdate: Boolean = true

  override def continueUpdate: Boolean = true
}
