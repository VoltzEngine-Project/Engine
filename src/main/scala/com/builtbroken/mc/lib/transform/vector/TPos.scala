package com.builtbroken.mc.lib.transform.vector

import com.builtbroken.jlib.data.vector.IPos2D
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.Entity

/**
 * Trait designed to be applied to objects that want to be IVector2
 *
 * Created by robert(Darkguardsman) on 9/25/2014.
 */
trait TPos extends IPos2D
{
  override def x: Double =
  {
    if(this.isInstanceOf[TileEntity])
    {
      return this.asInstanceOf[TileEntity].xCoord
    }
    else if(this.isInstanceOf[Entity])
    {
      return this.asInstanceOf[Entity].posX
    }
    return 0;
  }

  def xi : Int = x.asInstanceOf[Int]

  override def y: Double =
  {
    if(this.isInstanceOf[TileEntity])
    {
      return this.asInstanceOf[TileEntity].yCoord
    }
    else if(this.isInstanceOf[Entity])
    {
      return this.asInstanceOf[Entity].posY
    }
    return 0;
  }

  def yi : Int = y.asInstanceOf[Int]

  /** Gets this object as a Vector2 */
  def toVector2 : Point = new Point(x(), y())

  def distance2D( vec : IPos2D) : Double = toVector2.distance(new Point(vec.x(), vec.y()))
}
