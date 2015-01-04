package com.builtbroken.lib.transform.region

import java.util

import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import com.builtbroken.lib.transform.vector.{IVector3, Vector3}
import com.builtbroken.lib.wrapper.ByteBufWrapper._

/** 3D Ball shaped region in the world.
  * Can be used for collision boxes, and entity detection
  *
  * Created by robert on 12/18/2014.
  */
class Sphere(c: IVector3, var r: Double) extends Shape3D[Sphere](c)
{
  override def set(other: Sphere): Sphere =
  {
    super.set(other)
    this.r = other.r
    return this
  }

  override def +(amount: Double): Sphere = new Sphere(center, r + amount)

  override def +(amount: Sphere): Sphere = new Sphere(new Vector3(center).midPoint(amount.center), r + amount.r)

  override def *(amount: Double): Sphere = new Sphere(center, r * amount)

  override def *(amount: Sphere): Sphere = new Sphere(new Vector3(center).midPoint(amount.center), r * amount.r)

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    super.writeByteBuf(data)
    data <<< r
    return data
  }

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    super.writeNBT(nbt)
    nbt.setDouble("radius", r)
    return nbt
  }

  override def getSizeX: Double = r * 2

  override def getSizeY: Double = r * 2

  override def getSizeZ: Double = r * 2

  override def getArea: Double = 4 * Math.PI * (r * r)

  override def getVolume: Double = (4 * Math.PI * (r * r * r)) / 3

  override def isWithin(x: Double, y: Double, z: Double): Boolean = new Vector3(x, y, z).subtract(x, y, z).magnitude <= this.r

  def getEntities[E <: Entity](world: World, clazz: Class[E]): java.util.List[E] =
  {
    val list: java.util.List[E] = new util.ArrayList[E]
    val minX: Int = MathHelper.floor_double((r - World.MAX_ENTITY_RADIUS) / 16.0D)
    val maxX: Int = MathHelper.floor_double((r + World.MAX_ENTITY_RADIUS) / 16.0D)
    val minZ: Int = MathHelper.floor_double((r - World.MAX_ENTITY_RADIUS) / 16.0D)
    val maxZ: Int = MathHelper.floor_double((r + World.MAX_ENTITY_RADIUS) / 16.0D)

    //world.getEntitiesWithinAABB()
    for (i1 <- minX to maxX)
    {
      for (j1 <- minZ to maxZ)
      {
        if (world.getChunkProvider.chunkExists(i1, j1))
        {
          val chunk = world.getChunkFromChunkCoords(i1, j1)

          var i = MathHelper.floor_double((r - World.MAX_ENTITY_RADIUS) / 16.0D);
          var j = MathHelper.floor_double((r + World.MAX_ENTITY_RADIUS) / 16.0D);
          i = MathHelper.clamp_int(i, 0, chunk.entityLists.length - 1);
          j = MathHelper.clamp_int(j, 0, chunk.entityLists.length - 1);

          for (k <- i to j)
          {
            val list1 = chunk.entityLists(k);

            for (l <- 0 to list1.size())
            {
              val entity = list1.get(l).asInstanceOf[Entity];

              if (ClassIsAssignableFrom.isAssignableFrom(clazz, entity.getClass()) && distance3D(new Vector3(entity)) <= r)
              {
                list.add(entity.asInstanceOf[E]);
              }
            }
          }
        }
      }
    }
    return list
  }

}
