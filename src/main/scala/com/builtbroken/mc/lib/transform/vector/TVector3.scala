package com.builtbroken.mc.lib.transform.vector

import com.builtbroken.jlib.data.vector.IPos3D
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity

/**
 * Trait designed to be applied to objects that want to be IVector3
 *
 * Created by robert(Darkguardsman) on 9/25/2014.
 */
trait TVector3 extends TPos with IPos3D
{
  override def z: Double =
  {
    if(this.isInstanceOf[TileEntity])
    {
      return this.asInstanceOf[TileEntity].zCoord
    }
    else if(this.isInstanceOf[Entity])
    {
      return this.asInstanceOf[Entity].posZ
    }
    return 0
  }

  def zi : Int = z.asInstanceOf[Int]

  /** Gets this object as a Vector3 */
  def toVector3 : Pos = new Pos(this)

  def distance3D( vec : IPos3D) : Double = toVector3.distance(new Pos(vec).asInstanceOf[IPos3D])
}
