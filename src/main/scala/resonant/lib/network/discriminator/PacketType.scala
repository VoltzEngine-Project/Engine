package resonant.lib.network.discriminator

import io.netty.buffer.Unpooled
import resonant.lib.network.ByteBufWrapper._
import resonant.lib.network.netty.AbstractPacket

/**
 * @author tgame14, Calclavia
 * @since 26/05/14
 */
abstract class PacketType extends AbstractPacket
{
  protected var data = Unpooled.buffer

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