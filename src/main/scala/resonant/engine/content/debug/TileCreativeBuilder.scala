package resonant.engine.content.debug

import java.util.Map.Entry
import java.util.{ArrayList, List}

import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import resonant.content.prefab.java.TileAdvanced
import resonant.engine.ResonantEngine
import resonant.lib.content.prefab.TRotatable
import resonant.lib.network.discriminator.{PacketTile, PacketType}
import resonant.lib.network.handle.TPacketIDReceiver
import resonant.lib.schematic.Schematic
import universalelectricity.core.transform.vector.Vector3

/**
 * Automatically set up structures to allow easy debugging in creative mode.
 */
object TileCreativeBuilder
{
    val registry: List[Schematic] = new ArrayList[Schematic]

    def register(schematic: Schematic): Int =
    {
        registry.add(schematic)
        return registry.size - 1
    }

}

class TileCreativeBuilder extends TileAdvanced(Material.iron) with TRotatable with TPacketIDReceiver
{
    //Current build task vars
    var doBuild: Boolean = true
    var buildMap: java.util.HashMap[Vector3, Block] = null
    var buildLimit = 20
    //Gui vars
    var schematicID = 0
    var size = 0

    //Constructor
    creativeTab = CreativeTabs.tabTools
    rotationMask = Integer.parseInt("111111", 2).toByte

    override def update()
    {
        super.update()
        val buildThisUpdate = Math.min(buildLimit, buildMap.size())
        if (buildThisUpdate > 0)
        {
            for (i <- 0 until buildThisUpdate)
            {
                val entry: Entry[Vector3, Block] = buildMap.entrySet().toArray()(i).asInstanceOf[Entry[Vector3, Block]]
                val placement = this.position + entry.getKey
                placement.setBlock(world, entry.getValue)
                buildMap.remove(entry.getKey)
            }
        }
        if (buildThisUpdate <= 0)
        {
            doBuild = false
            buildMap = null
            sendDescPacket
        }
    }

    /**
     * Called when the block is right clicked by the player
     */
    override def activate(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
    {
        player.openGui(ResonantEngine.instance, -1, world, x.asInstanceOf[Int], y.asInstanceOf[Int], z.asInstanceOf[Int])
        return true
    }

    override def getDescPacket: PacketTile = {
        return new PacketTile(x.asInstanceOf[Int], y.asInstanceOf[Int], z.asInstanceOf[Int], Array(1, schematicID, size, doBuild))
    }

    override def read(data: ByteBuf, packetID: Int, player: EntityPlayer, packet: PacketType)
    {
        if (!world.isRemote)
        {
            if (!doBuild && player.capabilities.isCreativeMode)
            {
                schematicID = data.readInt
                size = data.readInt
                //TODO check for packet spamming as this could be abused by players to create a lag machine
                sendDescPacket
            }
        }
        else if(packetID == 1)
        {
            schematicID = data.readInt()
            size = data.readInt()
            doBuild = data.readBoolean()
        }
    }

}