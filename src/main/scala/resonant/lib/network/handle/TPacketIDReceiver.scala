package resonant.lib.network.handle

import cpw.mods.fml.common.network.ByteBufUtils
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import resonant.lib.network.discriminator.PacketType
import universalelectricity.api.core.grid.ISave

/**
 * Mixin prefab designed to implement basic packet handling & helper methods
 *
 * Handles NBTTagCompound packets for methods TileEntity.readFromNBT and ISave.load
 * with packet ID 0.
 *
 * Created on 8/6/2014.
 * @author robert(Darkguardsman)
 */
trait TPacketIDReceiver extends TPacketReceiver with IPacketIDReceiver
{
    override def read(buf: ByteBuf, id: Int, player: EntityPlayer, `type`: PacketType): Boolean =
    {
        if (id == 0)
        {
            if (this.isInstanceOf[TileEntity])
            {
                this.asInstanceOf[TileEntity].readFromNBT(ByteBufUtils.readTag(buf))
                return true
            }
            else if (this.isInstanceOf[ISave])
            {
                this.asInstanceOf[ISave].load(ByteBufUtils.readTag(buf))
                return true
            }
        }
        return false
    }

}
