package resonant.lib.access.scala

import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import resonant.lib.access.java.Permission

class AccessGroup extends AbstractAccess
{
  var users = Set.empty[AccessUser]

  def this(nbt: NBTTagCompound)
  {
    this()
    val nbtList = nbt.getTagList("users", 10)
    users = ((0 until nbtList.tagCount()) map (i => new AccessUser(nbtList.getCompoundTagAt(i)))).toSet
    fromNBT(nbt)
  }

  override def toNBT: NBTTagCompound =
  {
    val nbt = super.toNBT

    val userList = new NBTTagList()
    users.foreach(x => userList.appendTag(x.toNBT))
    nbt.setTag("users", userList)
    return nbt
  }

  override def hasPermission(username: String, permission: Permission): Boolean =
  {
    return users.exists(_.username.equals(username)) && (permissions.contains(permission) || users.exists(_.hasPermission(permission)))
  }
}