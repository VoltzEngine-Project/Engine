package resonant.lib.network

import java.util.Set

import net.minecraft.entity.player.EntityPlayer

/**
 * TPlayerUsing is a trait applied to all tiles that can have GUI. It is used to indicate which players the tile needs to send a packet too. The set provided will be mutated by the ContainerBase class in the GUI.
 */
trait IPlayerUsing
{
  def getPlayersUsing: Set[EntityPlayer]
}