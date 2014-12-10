package resonant.lib.access.scala

import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import resonant.lib.access.java.Permission

/**
 * An access holder that holds a set of groups, with each group containing users.
 * @author Calclavia
 */

class AccessHolder
{
  var groups = Set.empty[AccessGroup]

  def this(nbt: NBTTagCompound)
  {
    this()
    val nbtList = nbt.getTagList("groups", 10)
    groups = ((0 until nbtList.tagCount()) map (i => new AccessGroup(nbtList.getCompoundTagAt(i)))).toSet
  }

  def toNBT: NBTTagCompound =
  {
    val nbt = new NBTTagCompound()
    val nbtList = new NBTTagList()
    groups.foreach(group => nbtList.appendTag(group.toNBT))
    nbt.setTag("groups", nbtList)
    return nbt
  }

  def hasPermission(username: String, permission: Permission): Boolean = return groups.exists(_.hasPermission(username, permission))
}






