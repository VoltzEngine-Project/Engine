package com.builtbroken.lib.network.packet

import io.netty.buffer.Unpooled
import net.minecraft.entity.player.EntityPlayer
import AbstractPacket
import com.builtbroken.lib.wrapper.ByteBufWrapper._

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