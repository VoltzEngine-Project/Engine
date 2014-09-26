package universalelectricity.core.transform.vector

import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity

/**
 * Trait designed to be applied to objects that want to be IVector3
 *
 * Created by robert(Darkguardsman) on 9/25/2014.
 */
trait TVector3 extends TVector2 with IVector3
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
    return 0;
  }

  /** Gets this object as a Vector3 */
  def asVector3 : Vector3 = new Vector3(this)

  def distance3D( vec : IVector3) : Double = asVector3.distance(new Vector3(vec))
}
