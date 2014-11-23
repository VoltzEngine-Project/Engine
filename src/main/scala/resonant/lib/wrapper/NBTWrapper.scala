package resonant.lib.wrapper

import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import resonant.lib.utility.nbt.NBTUtility

import scala.reflect.ClassTag

/**
 * @author Calclavia
 */
object NBTWrapper
{

  implicit class WrappedNBT(underlying: NBTTagCompound)
  {
    def get(key: String) = NBTUtility.loadObject(underlying, key)

    def set(key: String, value: Any)
    {
      NBTUtility.saveObject(underlying, key, value)
    }

    def getArray[T: ClassTag](name: String): Array[T] =
    {
      val tagList = underlying.getTagList(name, 10)
      var seq = Seq.empty[T]

      for (i <- 0 until tagList.tagCount)
      {
        val innerTag = tagList.getCompoundTagAt(i)

        if (NBTUtility.loadObject(innerTag, "value").isInstanceOf[T])
          seq :+= NBTUtility.loadObject(innerTag, "value").asInstanceOf[T]
        else
          seq :+= null.asInstanceOf[T]
      }

      return seq.toArray
    }

    def setArray(name: String, arr: Array[_])
    {
      val tagList: NBTTagList = new NBTTagList

      for (i <- 0 until arr.length)
      {
        val innerTag = new NBTTagCompound
        if (arr(i) != null)
          NBTUtility.saveObject(innerTag, "value", arr(i))
        tagList.appendTag(innerTag)
      }

      underlying.setTag(name, tagList)
    }
  }

}
