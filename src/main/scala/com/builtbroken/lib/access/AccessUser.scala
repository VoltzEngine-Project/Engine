package com.builtbroken.lib.access

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound

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

  override def hasPermission(username: String, permission: Permission): Boolean = hasPermission(permission)

  def hasPermission(permission: Permission): Boolean = permissions.contains(permission)
}