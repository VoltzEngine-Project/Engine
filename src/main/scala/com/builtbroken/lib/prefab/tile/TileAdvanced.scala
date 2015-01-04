package com.builtbroken.lib.prefab.tile

import net.minecraft.block.material.Material
import net.minecraft.network.Packet
import com.builtbroken.lib.prefab.tile.traits.TRotatable
import com.builtbroken.mod.BBL
import com.builtbroken.lib.network.netty.AbstractPacket
import com.builtbroken.lib.prefab.tile.spatial.SpatialTile

class TileAdvanced(material: Material) extends SpatialTile(material: Material) with TRotatable
{
  override def getDescriptionPacket: Packet =
  {
    BBL.instance.packetHandler.toMCPacket(getDescPacket)
  }

  def getDescPacket: AbstractPacket = null
}
