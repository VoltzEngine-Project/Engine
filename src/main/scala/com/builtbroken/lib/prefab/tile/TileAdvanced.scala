package com.builtbroken.lib.prefab.tile

import com.builtbroken.lib.network.packet.AbstractPacket
import com.builtbroken.lib.prefab.tile.spatial.SpatialTile
import com.builtbroken.mod.BBL
import net.minecraft.block.material.Material
import net.minecraft.network.Packet

class TileAdvanced(material: Material) extends SpatialTile(material: Material)
{
  override def getDescriptionPacket: Packet =
  {
    BBL.instance.packetHandler.toMCPacket(getDescPacket)
  }

  def getDescPacket: AbstractPacket = null
}
