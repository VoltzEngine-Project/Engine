package resonant.lib.access.scala

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import resonant.lib.access.java.Permission

class AccessUser(var username: String) extends AbstractAccess
{
  def this(nbt: NBTTagCompound)
  {
    this(nbt.getString("username"))
    fromNBT(nbt)
  }

  def this(player: EntityPlayer)
  {
    this(player.getGameProfile.getName)
  }

  override def toNBT: NBTTagCompound =
  {
    val nbt = super.toNBT
    nbt.setString("username", username)
    return nbt
  }

  def hasPermission(permission: Permission): Boolean = permissions.contains(permission)
}