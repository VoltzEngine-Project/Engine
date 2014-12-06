package resonant.engine.network.discriminator

import io.netty.buffer.Unpooled
import net.minecraft.entity.player.EntityPlayer
import resonant.engine.network.ByteBufWrapper._
import resonant.engine.network.netty.AbstractPacket

/**
 * @author tgame14, Calclavia
 * @since 26/05/14
 */
abstract class PacketType extends AbstractPacket
{
  var data = Unpooled.buffer
  var sender: EntityPlayer = null

  def this(args: Array[AnyRef])
  {
    this()

    if (args != null)
      this <<< args
  }

  def write(arg: Any) = this <<< arg

  def <<<(arg: Any) =
  {
    data <<< arg
    this
  }
}