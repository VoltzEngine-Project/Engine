package resonant.content.loader

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
                val name = if (f.getAnnotation(classOf[ImplicitContentName]) != null) f.getName else null

                f.get(self) match
                {
                  case item: Item =>
                    if (name != null) manager.newItem(name, item) else manager.newItem(item)
                  case block: SpatialBlock =>
                    if (name != null) manager.newBlock(name, block) else manager.newBlock(block)
                  case _ =>
                }
              })
  }
}