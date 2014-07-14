package resonant.content.loader

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.Item
import resonant.content.spatial.block.SpatialBlock

/**
 * Automatic content registration for all fields extending this trait.
 * @author anti344, Calclavia
 */
trait ContentLoader
{
  self =>

  lazy val manager = new ModManager()

  def preInit() =
  {
    self.getClass.getDeclaredFields
      .foreach(
        f =>
        {
          f.setAccessible(true)
          val annotation = f.getAnnotation(classOf[ExplicitContentName])
          val name = if (annotation != null) (if (annotation.value != null) annotation.value else f.getName) else null

          f.get(self) match
          {
            case item: Item =>
              f.set(self, if (name != null) manager.newItem(name, item) else manager.newItem(item))
            case block: DummySpatialBlock =>
              f.set(self, if (name != null) manager.newBlock(name, block.spatial) else manager.newBlock(block.spatial))
            case _ => //println("ContentLoader attempted to create an object that is not a block or item: " + f)
          }
        })
  }

  /**
   * Creates a dummy block temporarily until the preInit stage is passed
   */
  implicit protected def wrapSpatialToBlock(spatial: SpatialBlock): Block = new DummySpatialBlock(spatial)

  protected class DummySpatialBlock(val spatial: SpatialBlock) extends Block(Material.air)
  {

  }

}