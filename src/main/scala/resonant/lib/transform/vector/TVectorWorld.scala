package resonant.lib.transform.vector

import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 * Trait designed to be applied to objects that want to be IVectorWorld
 *
 * Created by robert(Darkguardsman) on 9/25/2014.
 */
trait TVectorWorld extends TVector3 with IVectorWorld
{
  override def world: World =
  {
    if(this.isInstanceOf[TileEntity])
    {
      return this.asInstanceOf[TileEntity].getWorldObj
    }
    else if(this.isInstanceOf[Entity])
    {
      return this.asInstanceOf[Entity].worldObj
    }
    return null
  }

  /** Gets this object as a VectorWorld */
  def toVectorWorld : VectorWorld = new VectorWorld(this)
}
